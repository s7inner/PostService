package ua.moisak.PostService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.moisak.PostService.models.Shipment;

import java.util.List;
import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {

    @Query("SELECT s FROM Shipment s WHERE s.status = 'PENDING' ORDER BY s.id DESC")
    List<Shipment> findAllInDecOrderForStatusPending();



    @Query("SELECT s FROM Shipment s WHERE s.status = 'TAKEN' and s.performer_id = :performer_id ORDER BY s.id DESC")
    List<Shipment> findAllById_TAKEN_InDecOrder(@Param("performer_id") Integer performer_id);

    @Query("SELECT s FROM Shipment s WHERE s.status = 'IN_TRANSIT' and s.performer_id = :performer_id ORDER BY s.id DESC")
    List<Shipment> findAllById_IN_TRANSIT_InDecOrder(@Param("performer_id") Integer performer_id);

    @Query("SELECT s FROM Shipment s WHERE s.status = 'DELIVERED' and s.performer_id = :performer_id ORDER BY s.id DESC")
    List<Shipment> findAllById_DELIVERED_InDecOrder(@Param("performer_id") Integer performer_id);


    @Query("SELECT s FROM Shipment s WHERE s.person.id = :personId ORDER BY s.id DESC")
    List<Shipment> findAllInDecOrderForCurrentPerson(@Param("personId") Integer personId);

    @Modifying
    @Query("UPDATE Shipment s SET s.senderEmail = :newEmail WHERE s.person.id = :personId")
    void updateSenderEmail(@Param("newEmail") String newEmail, @Param("personId") Integer personId);
    void deleteById(Integer id);
}