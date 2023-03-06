package ua.moisak.PostService.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.models.Profile;
import ua.moisak.PostService.repositories.PeopleRepository;
import ua.moisak.PostService.repositories.ProfileRepository;

import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final PersonDetailsService personDetailsService;
    private final PeopleRepository peopleRepository;


    public ProfileService(ProfileRepository profileRepository, PersonDetailsService personDetailsService, PeopleRepository peopleRepository) {
        this.profileRepository = profileRepository;
        this.personDetailsService = personDetailsService;
        this.peopleRepository = peopleRepository;
    }

    @Transactional

    public void createProfile(Profile profile) {
        Person person = personDetailsService.getCurrentUser().get();

        //якщо person вже має профіль
        if(person.getProfile()!=null){
            profileRepository.deleteById(person.getProfile().getId());
        }

        //якщо користувач оновив email -> змінити username в Person
        if (!person.getUsername().equals(profile.getEmail())){
            person.setUsername(profile.getEmail());
           peopleRepository.save(person);


        }

        profile.setPerson(person);
        profileRepository.save(profile);
    }

}



