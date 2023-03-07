package ua.moisak.PostService.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.models.Profile;
import ua.moisak.PostService.services.PersonDetailsService;
import ua.moisak.PostService.services.ProfileService;
import ua.moisak.PostService.services.RegistrationService;
import ua.moisak.PostService.services.ShipmentService;
import ua.moisak.PostService.util.PersonValidator;

import javax.validation.Valid;

@Controller
public class HomeController {

    private final PersonDetailsService personDetailsService;
    private final ProfileService profileService;
    private final PersonValidator personValidator;
    private final RegistrationService registrationService;

    private final ShipmentService shipmentService;


    public HomeController(PersonDetailsService personDetailsService, ProfileService profileService, PersonValidator personValidator, RegistrationService registrationService, ShipmentService shipmentService) {
        this.personDetailsService = personDetailsService;
        this.profileService = profileService;
        this.personValidator = personValidator;
        this.registrationService = registrationService;
        this.shipmentService = shipmentService;
    }

    @GetMapping("")
    public String getHome() {
        return "home/home";
    }


    @GetMapping("/changeCredentials")
    @PreAuthorize("isAuthenticated()")
    public String getChangeCredentials(Model model) {
        model.addAttribute("person", new Person());
        return "/home/changeCredentials";
    }

    @PostMapping("/changeCredentials")
    @PreAuthorize("isAuthenticated()")
    public String postChangeCredentials(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        Person currentUser = personDetailsService.getCurrentUser();
        String[] passwords = personDetailsService.getListFromString(person.getPassword());
//        String[] logins = personDetailsService.getListFromString(person.getUsername());

        //null or empty check
        if(person.getPassword()!=null && !person.getPassword().isEmpty()){
//            Profile profile = currentUser.getProfile();
//            List<Shipment> shipments = currentUser.getShipments();

            //changing login
            if(!currentUser.getUsername().equals(person.getUsername()) && passwords.length==1){
//                //перевіряє чи юзер з такии ім'ям уже існує
//                personValidator.validate(person, bindingResult);
//                if (bindingResult.hasErrors())
//                    return "/home/changeCredentials";

                currentUser.setUsername(person.getUsername());
                shipmentService.updateEmail(currentUser);
                personDetailsService.save(currentUser);
                return "redirect:/auth/login";
            }
            else if(passwords.length==2 && passwords[0].equals(passwords[1]) && !currentUser.getUsername().equals(person.getUsername())){
                currentUser.setPassword(registrationService.encodePassword(passwords[0]));
                personDetailsService.save(currentUser);

                return "redirect:/auth/login";
            }else if(passwords[0].equals(passwords[1])){
                currentUser.setUsername(person.getUsername());
                currentUser.setPassword(registrationService.encodePassword(passwords[0]));

                shipmentService.updateEmail(currentUser);
                personDetailsService.save(currentUser);

                return "redirect:/auth/login";
            }
        }




//        if(person.getUsername()==null || person.getUsername().isEmpty()){
//            person.setUsername(currentUser.getUsername());
//        }
//        if(person.getPassword()==null || person.getPassword().isEmpty()){
//            person.setPassword(currentUser.getPassword());
//            person.setRole(currentUser.getRole());
//            person.setProfile(currentUser.getProfile());
//            person.setShipments(currentUser.getShipments());
//
//            personDetailsService.delete(currentUser);
//            personDetailsService.save(person);
//
//        }else {
//            person.setRole(currentUser.getRole());
//            person.setProfile(currentUser.getProfile());
//            person.setShipments(currentUser.getShipments());
//
//            personDetailsService.delete(currentUser);
//            registrationService.register(person);
//        }

        return "redirect:/auth/login";
    }

    @Transactional // add
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String getProfile(Model model) {
        Person person = personDetailsService.getCurrentUser();
        if (person.getProfile() == null) {
            model.addAttribute("profile", new Profile(person.getUsername()));
        } else {
            model.addAttribute("profile", person.getProfile());
        }

        return "/home/profile";
    }

    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String postProfile(@ModelAttribute("profile") Profile profile) {
        profileService.createProfile(profile);
        return "/home/profile";

    }


}
