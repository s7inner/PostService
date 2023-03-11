package ua.moisak.PostService.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.moisak.PostService.enums.PerformerProfileStatus;
import ua.moisak.PostService.models.Profile;
import ua.moisak.PostService.models.Shipment;
import ua.moisak.PostService.services.PersonDetailsService;
import ua.moisak.PostService.services.ProfileService;
import ua.moisak.PostService.services.ShipmentService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ShipmentService shipmentService;
    private final ProfileService profileService;


    public AdminController(ShipmentService shipmentService, ProfileService profileService) {
        this.shipmentService = shipmentService;
        this.profileService = profileService;
    }

    // GET all shipments
    @GetMapping("list")
    public String getAllShipments(Model model) {
        List<Profile> profiles = profileService.findAll();
        model.addAttribute("profiles", profiles);
        return "admin/listForValidation";
    }

    @GetMapping("/{id}")
    public String getProfiletById(@PathVariable Integer id, Model model) {
        Profile profile = profileService.findById(id);
        model.addAttribute("profile", profile);
        return "admin/pagePerformer";
    }

    @PostMapping("/validate/{id}")
    public String validateProfile(@PathVariable Integer id) {
        Profile profile = profileService.findById(id);

        //в користувача, який має замовлення статус змінити не можна
        if(profile.getPerson().getShipments().size()==0){
            if(profile.getStatus().equals(PerformerProfileStatus.VALIDATED)){
                profile.setStatus(PerformerProfileStatus.NOT_VALIDATED);
            }else {
                profile.setStatus(PerformerProfileStatus.VALIDATED);
            }
            profileService.save(profile);
        }
        return "redirect:/admin/list";
    }



//    // Creating new Shipment--------------------------------------------------
//    @Transactional
//    @GetMapping("/new")
//    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
//    public String showNewShipmentForm(Model model) {
//        Person person = personDetailsService.getCurrentUser();
//        Profile profile = person.getProfile();
//
//        //Якщо немає створених Shipments, для заповнення даними форми, зтягуємо дані з профілю
//        //Якщо є, стягуємо останнє відправлення й заповнюємо ним форму
//        Shipment shipment = new Shipment();
//        if(person.shipmentsIsEmpty()&&profile!=null){
//            shipment = shipmentService.getShipmentForGetMappingFillProfileData(profile);
//
//            model.addAttribute("shipment",shipment);
//        }else if(!person.shipmentsIsEmpty()) {
//            model.addAttribute("shipment", shipmentService.findLastShipment());
//        }else {
//            shipment.setSenderEmail(person.getUsername());
//            model.addAttribute("shipment",shipment);
//        }
//        return "/shipments/new";
//    }
//
//    @PostMapping("/new")
//    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
//    public String createNewShipment(@ModelAttribute("shipment") Shipment shipment,
//                                    @RequestParam("photo") MultipartFile photo) throws IOException {
//
//        shipmentService.save(setAdditionFields(shipment,photo));
//        return "/shipments/new";
//    }
//
//    //DELETE shipment by id using @RequestParam - get object from model with attribute name = "", with value id


    //UPDATE shipment by id using @PathVariable - get id from url, which recive from th:action="some url + id"
//    @GetMapping("/update/{id}")
//    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
//    public String getModifyShipmentPage(@PathVariable Integer id, Model model) {
//        Shipment shipment = shipmentService.findById(id);
//        model.addAttribute("shipment", shipment);
//
//        return "/shipments/update";
//    }
//
//    @PostMapping("/update/{id}")
//    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
//    public String modifyShipment(@PathVariable Integer id,
//                                 @ModelAttribute Shipment shipment,
//                                 @RequestParam("photo") MultipartFile photo) throws IOException {
//
//        shipmentService.save(setAdditionFields(shipment,photo));
//        return "redirect:/shipments/list";
//    }

//    public Shipment setAdditionFields(Shipment shipment, MultipartFile photo) throws IOException {
//        shipment.setLocalDateTime(shipment.getLocalDateTimeWithFormatter());
//        String[] stringsFonUnicueNumber = {
//                shipment.getSenderFullName(),
//                shipment.getSenderEmail(),
//                shipment.getSenderPhone(),
//                shipment.getOrigin(),
//
//                shipment.getRecipientFullName(),
//                shipment.getRecipientEmail(),
//                shipment.getRecipientPhone(),
//                shipment.getDestination(),
//                shipment.getLocalDateTime()
//        };
//
//        String inv = shipmentService.generateUniqueNumber(stringsFonUnicueNumber);
//        shipment.setInvoice(inv);
//        shipment.setStatus(ShipmentStatus.PENDING);
//        shipment.setWeightVolumetric(shipment.calculateWeightVolumetric());
//
//        //compress photo and set
//        shipment.setShipmentPhoto(Base64.getEncoder().encodeToString(photo.getBytes()));
//
//        Person person = personDetailsService.getCurrentUser();
//        shipment.setPerson(person);
//
//        return shipment;
//    }

}
