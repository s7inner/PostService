package ua.moisak.PostService.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ua.moisak.PostService.enums.PerformerProfileStatus;
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
    private final RegistrationService registrationService;

    private final ShipmentService shipmentService;


    public HomeController(PersonDetailsService personDetailsService, ProfileService profileService, RegistrationService registrationService, ShipmentService shipmentService) {
        this.personDetailsService = personDetailsService;
        this.profileService = profileService;
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
//        personValidator.validate(person, bindingResult);
//
//        if (bindingResult.hasErrors())
//            return "/home/changeCredentials";

        Person currentUser = personDetailsService.getCurrentUser();
        String[] passwords = personDetailsService.getListFromString(person.getPassword());

        // nothing to entered
        if (person.getUsername() == null && person.getPassword() == null) {
            return "home/changeCredentials";
        }

        // check if current password entered
        if (passwords[0].length() > 0) {
            boolean enteredAndCurrentPasswordsMath = registrationService.matches(passwords[0], currentUser.getPassword());

            // if matches passwords to enable change credentials
            if (enteredAndCurrentPasswordsMath) {

                //change login, check if login entered
                if (person.getUsername() != null && passwords.length == 1) {
                    //check if user exist with this username
                    if (personDetailsService.findByUsername(person.getUsername()) == null) {
                        //set entered email from form
                        currentUser.setUsername(person.getUsername());
                        //update email in other tables
                        profileService.updateEmail(currentUser);
                        shipmentService.updateEmail(currentUser);
                        //------------save in db-----------
                        personDetailsService.save(currentUser);
                        return "redirect:/auth/login";
                    }
                }
                //change password, check if password entered
                else if (person.getUsername().equals("") && passwords.length == 3) {

                    //check if newPassword and confirmPassword equal
                    if (passwords[1].equals(passwords[2])) {
                        currentUser.setPassword(registrationService.encodePassword(passwords[2]));
                        personDetailsService.save(currentUser);

                        return "redirect:/auth/login";
                    }

                } else if (!person.getUsername().equals("") && passwords.length == 3) {
                    if (personDetailsService.findByUsername(person.getUsername()) == null && passwords[1].equals(passwords[2])) {
                        //changing login and password
                        currentUser.setUsername(person.getUsername());
                        currentUser.setPassword(registrationService.encodePassword(passwords[1]));

                        profileService.updateEmail(currentUser);
                        shipmentService.updateEmail(currentUser);
                        personDetailsService.save(currentUser);

                        return "redirect:/auth/login";
                    }
                }
            }
        }
        return "home/changeCredentials";
    }

    //    @Transactional // add
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String getProfile(Model model) {
        Person person = personDetailsService.getCurrentUser();
        //if Profile dont exist
        if (person.getProfile() == null) {
            Profile profile = new Profile();

            //if its Performer user -> set status Validation
            if (person.getRole().equals("ROLE_PERFORMER")) {
                profile.setStatus(PerformerProfileStatus.NOT_VALIDATED);
            }
            profile.setEmail(person.getUsername());

            model.addAttribute("profile", profile);
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
