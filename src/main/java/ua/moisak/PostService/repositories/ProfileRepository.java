package ua.moisak.PostService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.moisak.PostService.models.Profile;
import ua.moisak.PostService.models.Shipment;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    @Query("SELECT p FROM Profile p ORDER BY p.id DESC")
    List<Profile> findAllInDecOrder();

    @Query("SELECT p FROM Profile p WHERE p.status='NOT_VALIDATED' ORDER BY p.id DESC")
    List<Profile> findAllInDecOrderForPerformers();

    @Modifying
    @Query("UPDATE Profile p SET p.email = :newEmail WHERE p.person.id = :personId")
    void updateSenderEmail(@Param("newEmail") String newEmail, @Param("personId") Integer personId);

}
