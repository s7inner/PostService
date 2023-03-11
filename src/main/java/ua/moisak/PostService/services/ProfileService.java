package ua.moisak.PostService.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.models.Profile;
import ua.moisak.PostService.models.Shipment;
import ua.moisak.PostService.repositories.PeopleRepository;
import ua.moisak.PostService.repositories.ProfileRepository;
import ua.moisak.PostService.util.LocalDataTimeUtil;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
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

    public Profile findById(Integer id){
        return profileRepository.findById(id).orElse(null);
    }

    public List<Profile> findAll() {
        return  profileRepository.findAll();
    }

    public List<Profile> findAllByDecOrder() {
        return  profileRepository.findAllInDecOrder();
    }

    public List<Profile> findAllByDecOrderForPerformers() {
        return  profileRepository.findAllInDecOrderForPerformers();
    }

    public void save(Profile profile) {
        profileRepository.save(profile);
    }

    public void createProfile(Profile profile) {
        Person person = personDetailsService.getCurrentUser();

        //якщо person вже має профіль, тоді для оновлення потрібно мати попередній id, тобто oldProfile
        if (person.getProfile() != null) {
            profile.setId(person.getProfile().getId());
            profile.setPerson(person);
            profile.setSendingTime(LocalDataTimeUtil.getLocalDateTimeWithFormatter());

            profileRepository.save(profile);
        } else {
            profile.setPerson(person);
            profile.setSendingTime(LocalDataTimeUtil.getLocalDateTimeWithFormatter());
            profileRepository.save(profile);
        }
    }

    public void updateEmail(Person person) {
        // update corresponding shipment senderEmail
        profileRepository.updateSenderEmail(person.getUsername(), person.getId());
    }


}



