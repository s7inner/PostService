package ua.moisak.PostService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.moisak.PostService.models.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

}
