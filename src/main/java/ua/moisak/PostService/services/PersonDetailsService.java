package ua.moisak.PostService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.repositories.PeopleRepository;
import ua.moisak.PostService.security.PersonDetails;

import java.util.Optional;

@Service
@Transactional
public class PersonDetailsService implements UserDetailsService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public Person findById(Integer id) {
        return peopleRepository.findById(id).orElse(null);
    }
    public Person findByUsername(String username) {
        return peopleRepository.findByUsername(username).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByUsername(s);

        if (person.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new PersonDetails(person.get());
    }


    public void delete(Person person) {
       peopleRepository.delete(person);
    }

    public void save(Person person) {
        peopleRepository.save(person);
    }

    public Person getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usermane = authentication.getName();
        return peopleRepository.findByUsername(usermane).orElse(null);
    }

    public String[] getListFromString(String str) {
        return  str.split(",");
    }
}
