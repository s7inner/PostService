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

    @PreAuthorize("hasAnyRole('ONLY_FOR_VIEW', 'ADMIN')")

    @GetMapping("list")
    public String getAllProfiles(Model model) {
        List<Profile> profiles = profileService.findAllByDecOrderForPerformers();
        model.addAttribute("profiles", profiles);
        return "admin/listForValidation";
    }
    @PreAuthorize("hasAnyRole('ONLY_FOR_VIEW', 'ADMIN')")

    @GetMapping("/{id}")
    public String getProfiletById(@PathVariable Integer id, Model model) {
        Profile profile = profileService.findById(id);
        model.addAttribute("profile", profile);
        return "admin/pagePerformer";
    }
    @PreAuthorize("hasAnyRole('ONLY_FOR_VIEW', 'ADMIN')")

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
}
