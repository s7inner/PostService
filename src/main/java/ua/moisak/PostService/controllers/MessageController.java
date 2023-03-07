package ua.moisak.PostService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.moisak.PostService.models.Message;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.repositories.MessageRepository;
import ua.moisak.PostService.services.MessageService;
import ua.moisak.PostService.services.PersonDetailsService;

import java.util.Optional;

@Controller
@RequestMapping("/messages")
public class MessageController {


    private final PersonDetailsService personDetailsService;
    private final MessageService messageService;


    public MessageController(PersonDetailsService personDetailsService, MessageService messageService) {
        this.personDetailsService = personDetailsService;
        this.messageService = messageService;
    }

    @GetMapping("/new")
    public String showNewMessageForm(Model model) {
        model.addAttribute("message", new Message());
        return "/messages/new";
    }

    @PostMapping("/new")
    public String createNewMessage(@ModelAttribute("message") Message message) {

        Person person = personDetailsService.getCurrentUser(); // get the currently authenticated user
        message.setAuthor(person); // set the author of the message
        messageService.save(message); // save the message to the database
        return "redirect:/messages/" + message.getId(); // redirect to the newly created message page
    }

}
