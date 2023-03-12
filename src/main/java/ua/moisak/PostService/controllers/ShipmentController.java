package ua.moisak.PostService.controllers;

import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.moisak.PostService.enums.PerformerProfileStatus;
import ua.moisak.PostService.enums.ShipmentStatus;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.models.Profile;
import ua.moisak.PostService.models.Shipment;
import ua.moisak.PostService.repositories.ShipmentRepository;
import ua.moisak.PostService.services.PersonDetailsService;
import ua.moisak.PostService.services.ShipmentService;
import ua.moisak.PostService.util.ImageUtil;
import ua.moisak.PostService.util.LocalDataTimeUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Controller
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final PersonDetailsService personDetailsService;

    public ShipmentController(ShipmentService shipmentService, PersonDetailsService personDetailsService) {
        this.shipmentService = shipmentService;
        this.personDetailsService = personDetailsService;
    }

    //FOR EMPLOYER---------------------------------------------------------------------------------------

    // GET all shipments
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ONLY_FOR_VIEW')")
    @GetMapping("/employer/list")
    public String getAllShipments(Model model) {
        List<Shipment> shipments = shipmentService.findAllInDescOrderForCurrentPerson(personDetailsService.getCurrentUser().getId());
        model.addAttribute("shipments", shipments);
        model.addAttribute("PENDING",ShipmentStatus.PENDING);

        return "/shipments/employer/list";
    }

    //GET Shipment by ID

    @GetMapping("/employer/{id}")
    public String getShipmentById(@PathVariable Integer id, Model model) {
        Shipment shipment = shipmentService.findById(id);

        model.addAttribute("shipment", shipment);
        return "/shipments/employer/shipment";
    }

    // GET Shipment for creating new
    @Transactional
    @GetMapping("/employer/new")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ONLY_FOR_VIEW')")
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
        return "/shipments/employer/new";
    }
    // POST Shipment for creating new
    @PostMapping("/employer/new")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ONLY_FOR_VIEW')")
    public String createNewShipment(@ModelAttribute("shipment") Shipment shipment,
                                    @RequestParam("photo") MultipartFile photo) throws IOException {

        shipmentService.save(shipmentService.setAdditionFields(shipment,photo));
        return "/shipments/employer/new";
    }

    //DELETE shipment by id using @RequestParam - get object from model with attribute name = "", with value id

    @PostMapping("/employer/delete")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ONLY_FOR_VIEW')")
    public String deleteShipment(@RequestParam("id") Integer id) {
        shipmentService.deleteById(id);
        return "redirect:/shipments/employer/list";
    }

    //UPDATE shipment by id using @PathVariable - get id from url, which recive from th:action="some url + id"
    @GetMapping("/employer/update/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ONLY_FOR_VIEW')")
    public String getModifyShipmentPage(@PathVariable Integer id, Model model) {
        Shipment shipment = shipmentService.findById(id);
        model.addAttribute("shipment", shipment);

        return "/shipments/employer/update";
    }

    @PostMapping("/employer/update/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ONLY_FOR_VIEW')")
    public String modifyShipment(@PathVariable Integer id,
                                 @ModelAttribute Shipment shipment,
                                 @RequestParam("photo") MultipartFile photo) throws IOException {

        shipmentService.save(shipmentService.setAdditionFields(shipment,photo));
        return "redirect:/shipments/employer/list";
    }
    //ROLE PERFORMER--------------------------------------------------------------------------------------------
    @PostMapping("/performer/list/{id}")
    public String takeShipmentByPerformer(@PathVariable Integer id) {
        Shipment shipment = shipmentService.findById(id);
        shipment.setStatus(ShipmentStatus.TAKEN);

        shipmentService.save(shipment);
        return "redirect:/shipments/performer/list";
    }

    @PostMapping("/performer/list/untake/{id}")
    public String setStatus_UNTAKE(@PathVariable Integer id) {
        Shipment shipment = shipmentService.findById(id);
        shipment.setStatus(ShipmentStatus.PENDING);

        shipmentService.save(shipment);
        return "redirect:/shipments/performer/list/taken";
    }
    @PostMapping("/performer/list/in_transit/{id}")
    public String setStatus_IN_TRANSIT(@PathVariable Integer id) {
        Shipment shipment = shipmentService.findById(id);
        shipment.setStatus(ShipmentStatus.IN_TRANSIT);

        shipmentService.save(shipment);
        return "redirect:/shipments/performer/list/taken";
    }
    @PostMapping("/performer/list/delivered/{id}")
    public String setStatus_DELIVERED(@PathVariable Integer id) {
        Shipment shipment = shipmentService.findById(id);
        shipment.setStatus(ShipmentStatus.DELIVERED);

        shipmentService.save(shipment);
        return "redirect:/shipments/performer/list/taken";
    }


    @PreAuthorize("hasAnyRole('PERFORMER', 'ONLY_FOR_VIEW')")
    @GetMapping("/performer/list")
    public String getAllShipmentsForPerformer(Model model) {
        List<Shipment> shipments = shipmentService.findAllInDecOrderForStatusPending();

        model.addAttribute("shipments", shipments);
        model.addAttribute("profile", personDetailsService.getCurrentUser().getProfile());
        model.addAttribute("VALIDATED", PerformerProfileStatus.VALIDATED);
        return "/shipments/performer/list";
    }

    @PreAuthorize("hasAnyRole('PERFORMER', 'ONLY_FOR_VIEW')")
    @GetMapping("/performer/list/taken")
    public String getAllTakenShipmentsForPerformer(Model model) {
        List<Shipment> shipmentsTAKEN = shipmentService.findAll_TAKEN_InDecOrder();
        model.addAttribute("shipmentsTAKEN", shipmentsTAKEN);

        List<Shipment> shipmentsIN_TRANSIT = shipmentService.findAll_IN_TRANSIT_InDecOrder();
        model.addAttribute("shipmentsIN_TRANSIT", shipmentsIN_TRANSIT);

        List<Shipment> shipmentsDELIVERED = shipmentService.findAll_DELIVERED_InDecOrder();
        model.addAttribute("shipmentsDELIVERED", shipmentsDELIVERED);

        model.addAttribute("profile", personDetailsService.getCurrentUser().getProfile());
        model.addAttribute("VALIDATED", PerformerProfileStatus.VALIDATED);
        return "/shipments/performer/listTaken";
    }
    }

