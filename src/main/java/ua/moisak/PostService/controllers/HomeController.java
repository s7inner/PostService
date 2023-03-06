package ua.moisak.PostService.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.models.Profile;
import ua.moisak.PostService.repositories.PeopleRepository;
import ua.moisak.PostService.services.PersonDetailsService;
import ua.moisak.PostService.services.ProfileService;
import ua.moisak.PostService.services.RegistrationService;
import ua.moisak.PostService.util.PersonValidator;

import javax.validation.Valid;

@Controller
public class HomeController {

    private final PersonDetailsService personDetailsService;
    private final ProfileService profileService;

    private final PeopleRepository peopleRepository;

    private final PersonValidator personValidator;
    private final RegistrationService registrationService;





    public HomeController(PersonDetailsService personDetailsService, ProfileService profileService, PeopleRepository peopleRepository, PersonValidator personValidator, RegistrationService registrationService) {
        this.personDetailsService = personDetailsService;
        this.profileService = profileService;
        this.peopleRepository = peopleRepository;
        this.personValidator = personValidator;
        this.registrationService = registrationService;
    }

    @GetMapping("")
    public String getHome() {
        return "home/home";
    }

    @GetMapping("/changeCredentials")
    @PreAuthorize("isAuthenticated()")
    public String getChangeCredentials(Model model) {
        Person person = personDetailsService.getCurrentUser().get();
        model.addAttribute("person", person);
        return "/home/changeCredentials";
    }

    @PostMapping("/changeCredentials")
    @PreAuthorize("isAuthenticated()")
    public String postChangeCredentials(@ModelAttribute("profile") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        Person currentUser = personDetailsService.getCurrentUser().get();

        if (bindingResult.hasErrors())
            return "home/changeCredentials";

        if(person.getUsername()==null || person.getUsername().isEmpty()){
            person.setUsername(currentUser.getUsername());
        }
        if(person.getPassword()==null || person.getPassword().isEmpty()){
            person.setPassword(currentUser.getPassword());
            person.setRole(currentUser.getRole());
            person.setProfile(currentUser.getProfile());
            peopleRepository.delete(currentUser);
            peopleRepository.save(person);

        }else {
            person.setRole(currentUser.getRole());
            person.setProfile(currentUser.getProfile());
            peopleRepository.delete(currentUser);
            registrationService.register(person);
        }

        return "redirect:/auth/login";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String getProfile(Model model) {
        Person person = personDetailsService.getCurrentUser().get();
        model.addAttribute("person", person);
        model.addAttribute("profile", new Profile());
        return "/home/profile";
    }

    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String postProfile(@ModelAttribute("profile") Profile profile) {
        profileService.createProfile(profile);
        return "/home/profile";

    }


}
