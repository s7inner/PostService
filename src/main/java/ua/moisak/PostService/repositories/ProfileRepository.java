package ua.moisak.PostService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.moisak.PostService.models.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    void deleteByEmail(String email);
}
