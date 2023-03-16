package ua.moisak.PostService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.moisak.PostService.models.Profile;
import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    @Query("SELECT p FROM Profile p ORDER BY p.id DESC")
    List<Profile> findAllInDecOrder();

    @Query("SELECT p FROM Profile p WHERE p.status='NOT_VALIDATED' ORDER BY p.id DESC")
    List<Profile> findAllInDecOrderForPerformers();

    @Modifying
    @Query("UPDATE Profile p SET p.email = :newEmail WHERE p.person.id = :personId")
    void updateSenderEmail(@Param("newEmail") String newEmail, @Param("personId") Integer personId);

    Optional<Profile> findByEmail(String email);

    @Modifying
    @Query("DELETE Profile p WHERE p.id = :id")
    void deleteById(@Param("id") Integer id);
}
