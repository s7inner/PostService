package ua.moisak.PostService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.moisak.PostService.models.Shipment;

public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {
}