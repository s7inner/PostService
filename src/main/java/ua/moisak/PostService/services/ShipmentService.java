package ua.moisak.PostService.services;

import org.springframework.stereotype.Service;

import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.models.Shipment;
import ua.moisak.PostService.repositories.PeopleRepository;
import ua.moisak.PostService.repositories.ShipmentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final PeopleRepository peopleRepository;

    public ShipmentService(ShipmentRepository shipmentRepository,PeopleRepository peopleRepository) {
        this.shipmentRepository = shipmentRepository;
        this.peopleRepository = peopleRepository;
    }

    // create a new person
    public Person createPerson(Person person) {
        return peopleRepository.save(person);
    }

    // get all people
    public List<Person> getAllPeople() {
        return peopleRepository.findAll();
    }

    // get a person by id
    public Optional<Person> getPersonById(Integer id) {
        return peopleRepository.findById(id);
    }

    // update a person
    public Person updatePerson(Person person) {
        return peopleRepository.save(person);
    }

    // delete a person by id
    public void deletePersonById(Integer id) {
        peopleRepository.deleteById(id);
    }

    // create a new shipment
    public Shipment createShipment(Integer personId, Shipment shipment) {
        Optional<Person> optionalPerson = peopleRepository.findById(personId);
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            shipment.setPerson(person);
            return shipmentRepository.save(shipment);
        } else {
            throw new IllegalArgumentException("Person with id " + personId + " not found");
        }
    }

    // get all shipments for a person
    public List<Shipment> getAllShipmentsForPerson(Integer personId) {
        Optional<Person> optionalPerson = peopleRepository.findById(personId);
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            return person.getShipments();
        } else {
            throw new IllegalArgumentException("Person with id " + personId + " not found");
        }
    }

    // get a shipment by id
    public Optional<Shipment> getShipmentById(Integer id) {
        return shipmentRepository.findById(id);
    }

    // update a shipment
    public Shipment updateShipment(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    // delete a shipment by id
    public void deleteShipmentById(Integer id) {
        shipmentRepository.deleteById(id);
    }
}
