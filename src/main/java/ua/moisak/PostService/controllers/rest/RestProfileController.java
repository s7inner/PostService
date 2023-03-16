package ua.moisak.PostService.controllers.rest;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.moisak.PostService.dto.EmployerProfileDTO;
import ua.moisak.PostService.dto.PerformerProfileDTO;
import ua.moisak.PostService.enums.PerformerProfileStatus;
import ua.moisak.PostService.models.Profile;
import ua.moisak.PostService.services.ProfileService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController // @Controller + @ResponseBody
@RequestMapping("/rest/profile")
public class RestProfileController {
    private final ProfileService profileService;
    private final ModelMapper modelMapper;


    public RestProfileController(ProfileService profileService, ModelMapper modelMapper) {
        this.profileService = profileService;
        this.modelMapper = modelMapper;
    }

    //FOR PERFORMER-----------------------------------------------------------
    @GetMapping("/performer")
    public ResponseEntity<PerformerProfileDTO> getProfileForCurrentPerformer(Principal principal) {
        Profile profile = profileService.findByEmail(principal.getName());
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(modelMapper.map(profile, PerformerProfileDTO.class), HttpStatus.OK);
    }

    @PostMapping("/performer/create")
    public ResponseEntity<String> createProfilePerformer(@RequestBody PerformerProfileDTO performerProfileDTO, Principal principal) {
        Profile profile = profileService.findByEmail(principal.getName());

        // Check if the user already has a profile
        if (profile != null) {
            return ResponseEntity.badRequest().body("User already has a profile!");
        }

        // Map the DTO to the entity
        profile = modelMapper.map(performerProfileDTO, Profile.class);
        profile = profileService.setAdditionalFields(profile);

        profileService.save(profile);
        return ResponseEntity.ok("Profile successfully created!");
    }

    @PutMapping("/performer/update")
    public ResponseEntity<String> updateProfileEmployer(@RequestBody PerformerProfileDTO performerProfileDTO, Principal principal) {
        Profile profile = profileService.findByEmail(principal.getName());
        performerProfileDTO.setId(profile.getId());

        // Check if the user already has a profile
        if (profile == null) {
            return ResponseEntity.badRequest().body("The user does not have a profile!");
        }

        // Map the DTO to the entity
        profile = modelMapper.map(performerProfileDTO, Profile.class);
        profile = profileService.setAdditionalFields(profile);

        profileService.save(profile);
        return ResponseEntity.ok("Profile successfully updated!");
    }
    @DeleteMapping("/performer/delete")
    public ResponseEntity<String> deleteMyProfilePerformer(Principal principal) {
        Profile profile = profileService.findByEmail(principal.getName());
        // Check if the user already has a profile
        if (profile == null) {
            return ResponseEntity.badRequest().body("The user does not have a profile!");
        }
        // Delete the user's profile
        profileService.deleteById(profile.getId());
        return ResponseEntity.ok("Profile successfully deleted!");
    }

    //FOR EMPLOYER-----------------------------------------------------------
    @GetMapping("/employer")
    public ResponseEntity<EmployerProfileDTO> getProfileForCurrentEmployer(Principal principal) {
        Profile profile = profileService.findByEmail(principal.getName());
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(modelMapper.map(profile, EmployerProfileDTO.class), HttpStatus.OK);
    }

    @PostMapping("/employer/create")
    public ResponseEntity<String> createProfileEmployer(@RequestBody EmployerProfileDTO employerProfileDTO, Principal principal) {
        Profile profile = profileService.findByEmail(principal.getName());

        // Check if the user already has a profile
        if (profile != null) {
            return ResponseEntity.badRequest().body("User already has a profile!");
        }

        // Map the DTO to the entity
        profile = modelMapper.map(employerProfileDTO, Profile.class);
        profile = profileService.setAdditionalFields(profile);

        profileService.save(profile);
        return ResponseEntity.ok("Profile successfully created!");
    }

    @PutMapping("/employer/update")
    public ResponseEntity<String> updateProfileEmployer(@RequestBody EmployerProfileDTO employerProfileDTO, Principal principal) {
        Profile profile = profileService.findByEmail(principal.getName());
        employerProfileDTO.setId(profile.getId());

        // Check if the user already has a profile
        if (profile == null) {
            return ResponseEntity.badRequest().body("The user does not have a profile!");
        }

        // Map the DTO to the entity
        profile = modelMapper.map(employerProfileDTO, Profile.class);
        profile = profileService.setAdditionalFields(profile);

        profileService.save(profile);
        return ResponseEntity.ok("Profile successfully updated!");
    }

    @DeleteMapping("/employer/delete")
    public ResponseEntity<String> deleteMyProfileEmployer(Principal principal) {
        Profile profile = profileService.findByEmail(principal.getName());
        // Check if the user already has a profile
        if (profile == null) {
            return ResponseEntity.badRequest().body("The user does not have a profile!");
        }

        // Delete the user's profile
        profileService.deleteById(profile.getId());
        return ResponseEntity.ok("Profile successfully deleted!");
    }

    //FOR ADMIN-----------------------------------------------------------
    @GetMapping("/admin/profiles")
    public ResponseEntity<List<PerformerProfileDTO>> getAllProfiles() {
        List<Profile> profiles = profileService.getAllProfiles();
        if(profiles.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<PerformerProfileDTO> profileDTOs = profiles.stream()
                .map(profile -> modelMapper.map(profile, PerformerProfileDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(profileDTOs);
    }

    @PatchMapping("/admin/validate/{id}")
        public ResponseEntity<String> validateProfilePerformer(@PathVariable Integer id) {
        Profile profile = profileService.findById(id);
        if (profile!=null) {
            profile.setStatus(PerformerProfileStatus.VALIDATED);
            profileService.save(profile);
            return ResponseEntity.ok("Performer successfully validated!");
        } else {
            return ResponseEntity.badRequest().body("Not found profile for performer with this id!");
        }
    }
}
