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

    public ProfileService(ProfileRepository profileRepository, PersonDetailsService personDetailsService) {
        this.profileRepository = profileRepository;
        this.personDetailsService = personDetailsService;
    }

    public void createProfile(Profile profile) {
        Person person = personDetailsService.getCurrentUser();

        //якщо person вже має профіль
        if (person.getProfile() != null) {
            Profile profileFromRelation = person.getProfile();

            profileFromRelation.setFullName(profile.getFullName());
            profileFromRelation.setPhone(profile.getPhone());
            profileFromRelation.setEmail(person.getUsername());
            profileFromRelation.setAddress(profile.getAddress());
            profileRepository.save(profileFromRelation);
        } else {
            profile.setPerson(person);
            profileRepository.save(profile);
        }
    }

    public void updateEmail(Person person) {
        // update corresponding shipment senderEmail
        profileRepository.updateSenderEmail(person.getUsername(), person.getId());
    }


}



