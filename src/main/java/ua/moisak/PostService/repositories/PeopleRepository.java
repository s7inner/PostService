package ua.moisak.PostService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.moisak.PostService.models.Person;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByUsername(String username);

    Optional<Person> findById(Integer id);

    void deleteByUsername(String username);

    @Modifying
    @Query("UPDATE Person p SET p.username = :newEmail WHERE p.username = :oldEmail")
    void updateEmail(@Param("oldEmail") String oldEmail, @Param("newEmail") String newEmail);
}
