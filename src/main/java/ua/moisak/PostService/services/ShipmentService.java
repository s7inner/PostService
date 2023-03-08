package ua.moisak.PostService.services;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.models.Profile;
import ua.moisak.PostService.models.Shipment;
import ua.moisak.PostService.repositories.PeopleRepository;
import ua.moisak.PostService.repositories.ShipmentRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final PeopleRepository peopleRepository;

    private final PersonDetailsService personDetailsService;


    public ShipmentService(ShipmentRepository shipmentRepository, PeopleRepository peopleRepository, PersonDetailsService personDetailsService) {
        this.shipmentRepository = shipmentRepository;
        this.peopleRepository = peopleRepository;
        this.personDetailsService = personDetailsService;
    }

    @PersistenceContext
    private EntityManager entityManager;

    public Shipment findLastShipment() {
        TypedQuery<Shipment> query = entityManager.createQuery(
                "SELECT s FROM Shipment s ORDER BY s.id DESC", Shipment.class);
        query.setMaxResults(1);
        List<Shipment> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }
        return new Shipment();
    }

    public List<Shipment> findAll() {
        return  shipmentRepository.findAll();
    }

    public Shipment getShipmentForGetMappingFillProfileData(Profile profile) {
        Shipment shipment = new Shipment();
        shipment.setSenderFullName(profile.getFullName());
        shipment.setSenderEmail(profile.getEmail());
        shipment.setSenderPhone(profile.getPhone());
        shipment.setOrigin(profile.getAddress());

        return shipment;
    }

    public static String generateUniqueNumber(String[] inputArray) {
        // concatenate all the strings in the input array into a single string
        String inputString = String.join("", inputArray);

        // hash the input string using SHA-256 algorithm
        byte[] hashedBytes = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            hashedBytes = messageDigest.digest(inputString.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // take the first 10 bytes of the hashed bytes array and convert it to a string of hexadecimal characters
        byte[] first10Bytes = Arrays.copyOf(hashedBytes, 8);
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : first10Bytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }
        String uniqueNumber = "IN"+ hexStringBuilder.toString().toUpperCase();

        return uniqueNumber;
    }

    public void save(Shipment shipment) {
        shipmentRepository.save(shipment);
    }

    public void updateEmail(Person person) {
        // update corresponding shipment senderEmail
        shipmentRepository.updateSenderEmail(person.getUsername(), person.getId());
    }
    // get all shipments for a person
//    public List<Shipment> getAllShipmentsForPerson(Integer personId) {
//        Optional<Person> optionalPerson = peopleRepository.findById(personId);
//        if (optionalPerson.isPresent()) {
//            Person person = optionalPerson.get();
//            return person.getShipments();
//        } else {
//            throw new IllegalArgumentException("Person with id " + personId + " not found");
//        }
//    }

    // get a shipment by id
//    public Optional<Shipment> getShipmentById(Integer id) {
//        return shipmentRepository.findById(id);
//    }
//
//    // update a shipment
//    public Shipment updateShipment(Shipment shipment) {
//        return shipmentRepository.save(shipment);
//    }
//
//    // delete a shipment by id
//    public void deleteShipmentById(Integer id) {
//        shipmentRepository.deleteById(id);
//    }
}
