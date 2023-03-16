package ua.moisak.PostService.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.moisak.PostService.enums.PerformerProfileStatus;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.models.Profile;
import ua.moisak.PostService.repositories.ProfileRepository;
import ua.moisak.PostService.util.LocalDataTimeUtil;
import java.util.List;

@Service
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final PersonDetailsService personDetailsService;

    public ProfileService(ProfileRepository profileRepository, PersonDetailsService personDetailsService) {
        this.profileRepository = profileRepository;
        this.personDetailsService = personDetailsService;
    }

    public List<Profile> getAllProfiles(){
        return profileRepository.findAll();
    }

    public Profile findByEmail(String email){
        return profileRepository.findByEmail(email).orElse(null);
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

    public Profile getCurrentProfile(){
        return personDetailsService.getCurrentUser().getProfile();
    }

    public void deleteById(Integer id){
        profileRepository.deleteById(id);
    }
    public Profile setAdditionalFields(Profile profile){
        Person person = personDetailsService.getCurrentUser();

        if(person.getRole().equals("ROLE_PERFORMER")){
            profile.setStatus(PerformerProfileStatus.NOT_VALIDATED);
        }

        profile.setEmail(person.getUsername());
        profile.setSendingTime(LocalDataTimeUtil.getLocalDateTimeWithFormatter());
        profile.setPerson(person);
        return profile;
    }


}



