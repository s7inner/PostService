package ua.moisak.PostService.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.moisak.PostService.enums.ShipmentStatus;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.models.Profile;
import ua.moisak.PostService.models.Shipment;
import ua.moisak.PostService.repositories.ShipmentRepository;
import ua.moisak.PostService.services.PersonDetailsService;
import ua.moisak.PostService.services.ShipmentService;
import ua.moisak.PostService.util.ImageUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final PersonDetailsService personDetailsService;

    public ShipmentController(ShipmentService shipmentService, PersonDetailsService personDetailsService) {
        this.shipmentService = shipmentService;
        this.personDetailsService = personDetailsService;
    }

    // GET all shipments
    @GetMapping("list")
    public String getAllShipments(Model model) {
        List<Shipment> shipments = shipmentService.findAllInDescOrder();
        model.addAttribute("shipments", shipments);
        return "shipments/list";
    }

    // Creating new Shipment--------------------------------------------------
    @Transactional
    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
    public String showNewShipmentForm(Model model) {
        Person person = personDetailsService.getCurrentUser();
        Profile profile = person.getProfile();

        //Якщо немає створених Shipments, для заповнення даними форми, зтягуємо дані з профілю
        //Якщо є, стягуємо останнє відправлення й заповнюємо ним форму
        Shipment shipment = new Shipment();
        if(person.shipmentsIsEmpty()&&profile!=null){
            shipment = shipmentService.getShipmentForGetMappingFillProfileData(profile);

            model.addAttribute("shipment",shipment);
        }else if(!person.shipmentsIsEmpty()) {
            model.addAttribute("shipment", shipmentService.findLastShipment());
        }else {
            shipment.setSenderEmail(person.getUsername());
            model.addAttribute("shipment",shipment);
        }
        return "/shipments/new";
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
    public String createNewShipment(@ModelAttribute("shipment") Shipment shipment,
                                    @RequestParam("photo") MultipartFile photo) throws IOException {

        shipmentService.save(setAdditionFields(shipment,photo));
        return "/shipments/new";
    }

    //DELETE shipment by id using @RequestParam - get object from model with attribute name = "", with value id

    @PostMapping("/delete")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
    public String deleteShipment(@RequestParam("id") Integer id) {
        shipmentService.deleteById(id);
        return "redirect:/shipments/list";
    }

    //UPDATE shipment by id using @PathVariable - get id from url, which recive from th:action="some url + id"
    @GetMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
    public String getModifyShipmentPage(@PathVariable Integer id, Model model) {
        Shipment shipment = shipmentService.findById(id);
        model.addAttribute("shipment", shipment);

        return "/shipments/update";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
    public String modifyShipment(@PathVariable Integer id,
                                 @ModelAttribute Shipment shipment,
                                 @RequestParam("photo") MultipartFile photo) throws IOException {

        shipmentService.save(setAdditionFields(shipment,photo));
        return "redirect:/shipments/list";
    }

    public Shipment setAdditionFields(Shipment shipment, MultipartFile photo) throws IOException {
        shipment.setLocalDateTime(shipment.getLocalDateTimeWithFormatter());
        String[] stringsFonUnicueNumber = {
                shipment.getSenderFullName(),
                shipment.getSenderEmail(),
                shipment.getSenderPhone(),
                shipment.getOrigin(),

                shipment.getRecipientFullName(),
                shipment.getRecipientEmail(),
                shipment.getRecipientPhone(),
                shipment.getDestination(),
                shipment.getLocalDateTime()
        };

        String inv = shipmentService.generateUniqueNumber(stringsFonUnicueNumber);
        shipment.setInvoice(inv);
        shipment.setStatus(ShipmentStatus.PENDING);
        shipment.setWeightVolumetric(shipment.calculateWeightVolumetric());

        //compress photo and set
        shipment.setShipmentPhoto(Base64.getEncoder().encodeToString(photo.getBytes()));

        Person person = personDetailsService.getCurrentUser();
        shipment.setPerson(person);

        return shipment;
    }

}
