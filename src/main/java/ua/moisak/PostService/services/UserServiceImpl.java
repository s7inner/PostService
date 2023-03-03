package ua.moisak.PostService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.repositories.PeopleRepository;

import java.util.Optional;

@Service
public class UserServiceImpl extends PersonDetailsService {

    @Autowired
    private PeopleRepository peopleRepository;

    public UserServiceImpl(PeopleRepository peopleRepository) {
        super(peopleRepository);
    }

    public Optional<Person> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usermane = authentication.getName();
        return peopleRepository.findByUsername(usermane);
    }
}
