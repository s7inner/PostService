package ua.moisak.PostService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.moisak.PostService.models.Message;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.repositories.MessageRepository;
import ua.moisak.PostService.services.UserServiceImpl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/new")
    public String showNewMessageForm(Model model) {
        model.addAttribute("message", new Message());
        return "/messages/new";
    }

    @PostMapping("/new")
    public String createNewMessage(@ModelAttribute("message") Message message) {

        Optional<Person> author = userServiceImpl.getCurrentUser(); // get the currently authenticated user
        message.setAuthor(author.get()); // set the author of the message
        messageRepository.save(message); // save the message to the database
        return "redirect:/messages/" + message.getId(); // redirect to the newly created message page
    }


    // other methods for displaying and managing messages

}
