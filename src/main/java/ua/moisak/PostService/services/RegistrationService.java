package ua.moisak.PostService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.repositories.PeopleRepository;

@Service
@Transactional
public class RegistrationService {

    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(Person person) {
        person.setPassword(encodePassword(person.getPassword()));
        save(person);
    }

    public String encodePassword(String password) {
        return  passwordEncoder.encode(password);
    }

    public void save(Person person){
        peopleRepository.save(person);
    }

}
