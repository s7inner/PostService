package ua.moisak.PostService.controllers.rest;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.moisak.PostService.dto.PerformerProfileDTO;
import ua.moisak.PostService.dto.PersonDTO;
import ua.moisak.PostService.enums.PerformerProfileStatus;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.models.Profile;
import ua.moisak.PostService.services.PersonDetailsService;
import ua.moisak.PostService.services.ProfileService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController // @Controller + @ResponseBody
@RequestMapping("/rest/user")
public class RestPersonController {
    private final PersonDetailsService personDetailsService;
    private final ModelMapper modelMapper;


    public RestPersonController(PersonDetailsService personDetailsService, ModelMapper modelMapper) {
        this.personDetailsService = personDetailsService;
        this.modelMapper = modelMapper;
    }


    //FOR ADMIN-----------------------------------------------------------
    @PreAuthorize("hasAnyRole('ADMIN', 'ONLY_FOR_VIEW')")
    @GetMapping("/admin/list")
    public ResponseEntity<List<PersonDTO>> getAllPeoples() {
        List<Person> people = personDetailsService.findAll();
        if(people.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        List<PersonDTO> personDTOS = people.stream()
                .map(p -> modelMapper.map(p, PersonDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(personDTOS);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ONLY_FOR_VIEW')")
    @GetMapping("/admin/{id}")
    public ResponseEntity<PersonDTO> getProfileForCurrentPerformer(@PathVariable Integer id) {
        Person person = personDetailsService.findById(id);
        if (person == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(modelMapper.map(person, PersonDTO.class), HttpStatus.OK);
    }
}
