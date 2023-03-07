package ua.moisak.PostService.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.moisak.PostService.models.Message;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.repositories.MessageRepository;


@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void save(Message message) {
        messageRepository.save(message);
    }

}

