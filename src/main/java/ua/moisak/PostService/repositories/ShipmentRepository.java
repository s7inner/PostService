package ua.moisak.PostService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.moisak.PostService.models.Shipment;

import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {

    @Modifying
    @Query("UPDATE Shipment s SET s.senderEmail = :newEmail WHERE s.person.id = :personId")
    void updateSenderEmail(@Param("newEmail") String newEmail, @Param("personId") Integer personId);

    @Query("SELECT s FROM Shipment s ORDER BY s.id DESC")
    List<Shipment> findAllInDescOrder();

//    @Modifying
//    @Query("DELETE FROM Shipment s WHERE s.senderEmail = :email")
//    int deleteBySenderEmail(@Param("email") String email);

}