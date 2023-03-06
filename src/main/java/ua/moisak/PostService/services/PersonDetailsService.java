package ua.moisak.PostService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.repositories.PeopleRepository;
import ua.moisak.PostService.security.PersonDetails;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByUsername(s);

        if (person.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new PersonDetails(person.get());
    }

    public Optional<Person> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usermane = authentication.getName();
        return peopleRepository.findByUsername(usermane);
    }

    public void updateEmail(String oldEmail, String newEmail) {
        peopleRepository.updateEmail(oldEmail, newEmail);
    }
}
