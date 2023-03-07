package ua.moisak.PostService.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.models.Profile;
import ua.moisak.PostService.repositories.PeopleRepository;
import ua.moisak.PostService.repositories.ProfileRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

@Service
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final PersonDetailsService personDetailsService;
    private final PeopleRepository peopleRepository;


    public ProfileService(ProfileRepository profileRepository, PersonDetailsService personDetailsService, PeopleRepository peopleRepository) {
        this.profileRepository = profileRepository;
        this.personDetailsService = personDetailsService;
        this.peopleRepository = peopleRepository;
    }


    public void createProfile(Profile profile) {
        Person person = personDetailsService.getCurrentUser();
        Profile profileFromRelation = person.getProfile();

        profile.setEmail(person.getUsername());

        //якщо person вже має профіль
        if(profileFromRelation!=null){
            deleteById(profileFromRelation.getId());
        }

        profile.setPerson(person);
        profileRepository.save(profile);
    }

    public void deleteById(Integer id) {
       profileRepository.deleteById(id);
    }

    public void updateEmail(Person person) {
        // update corresponding shipment senderEmail
        profileRepository.updateSenderEmail(person.getUsername(), person.getId());
    }


}



