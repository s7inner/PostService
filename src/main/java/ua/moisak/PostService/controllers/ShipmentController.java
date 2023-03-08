package ua.moisak.PostService.controllers;

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

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
        List<Shipment> shipments = shipmentService.findAll();
        model.addAttribute("shipments", shipments);
        return "shipments/list";
    }

    // Creating new Shipment--------------------------------------------------
    @Transactional
    @GetMapping("/new")
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
    public String createNewShipment(@ModelAttribute("shipment") Shipment shipment, @RequestParam("photo") MultipartFile photo) throws IOException {

        String[] stringsFonUnicueNumber = {
                shipment.getSenderFullName(),
                shipment.getSenderEmail(),
                shipment.getSenderPhone(),
                shipment.getOrigin(),

                shipment.getRecipientFullName(),
                shipment.getRecipientEmail(),
                shipment.getRecipientPhone(),
                shipment.getDestination(),
        };

        String inv = shipmentService.generateUniqueNumber(stringsFonUnicueNumber);
        shipment.setInvoice(inv);
        shipment.setStatus(ShipmentStatus.PENDING);
        shipment.setWeightVolumetric(shipment.calculateWeightVolumetric());
        shipment.setShipmentPhoto(photo.getBytes());
        shipment.setLocalDateTime(shipment.getLocalDateTimeWithFormatter());


        Person person = personDetailsService.getCurrentUser();
        shipment.setPerson(person);
        shipmentService.save(shipment);
        return "/shipments/new";
    }

    //-----------------------------------------------------------------------

//    @RequestMapping(value = "/shipment" ,method = RequestMethod.GET, headers = "Accept=application/json", produces =  "application/json" )

//    @GetMapping("/shipment")
//    public String shipmentnPage(@ModelAttribute("shipment") Shipment person) {
//        return "shipments/shipment";
//    }
////    @RequestMapping(value = "/shipment" ,method = RequestMethod.POST, headers = "Accept=application/json", produces =  "application/json" )
//
//    @PostMapping("/shipment")
//    public String saveShipment(@ModelAttribute("shipment") Shipment shipment) {
//        // Save the shipment object to the database using Hibernate
//        shipmentRepository.save(shipment);
//
//        // Redirect to the shipment list page
//        return "redirect:/shipments";
//    }
//
//
//    // GET a shipment by id
//    @GetMapping("/{id}")
//    public String getShipmentById(@PathVariable Integer id, Model model) {
//        Shipment shipment = shipmentRepository.findById(id)
//                .orElseThrow(null);
//        model.addAttribute("shipment", shipment);
//        return "shipments/view";
//    }
//
//    // POST a new shipment
//    @PostMapping("")
//    public String addShipment(@ModelAttribute("shipment") Shipment shipment) {
//        shipmentRepository.save(shipment);
//        return "redirect:/shipments";
//    }
//
//    // PUT (update) a shipment
//    @PutMapping("/{id}")
//    public String updateShipment(@PathVariable Integer id, @ModelAttribute("shipment") Shipment updatedShipment) {
//        Shipment shipment = shipmentRepository.findById(id)
//                .orElseThrow(null);
//        shipment.setInvoice(updatedShipment.getInvoice());
//        shipment.setSenderFullName(updatedShipment.getSenderFullName());
//        shipment.setSenderEmail(updatedShipment.getSenderEmail());
//        shipment.setSenderPhone(updatedShipment.getSenderPhone());
//        // set other fields
//
//        shipmentRepository.save(shipment);
//        return "redirect:/shipments/" + id;
//    }
//
//    // DELETE a shipment
//    @DeleteMapping("/{id}")
//    public String deleteShipment(@PathVariable Integer id) {
//        Shipment shipment = shipmentRepository.findById(id)
//                .orElseThrow(null);
//        shipmentRepository.delete(shipment);
//        return "redirect:/shipments";
//    }
}
