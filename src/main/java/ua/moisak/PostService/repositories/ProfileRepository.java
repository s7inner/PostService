package ua.moisak.PostService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.moisak.PostService.models.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    void deleteByEmail(String email);

    @Modifying
    @Query("UPDATE Profile p SET p.email = :newEmail WHERE p.person.id = :personId")
    void updateSenderEmail(@Param("newEmail") String newEmail, @Param("personId") Integer personId);

}
