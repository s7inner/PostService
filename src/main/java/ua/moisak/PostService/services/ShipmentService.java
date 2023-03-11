package ua.moisak.PostService.services;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.moisak.PostService.enums.ShipmentStatus;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.models.Profile;
import ua.moisak.PostService.models.Shipment;
import ua.moisak.PostService.repositories.PeopleRepository;
import ua.moisak.PostService.repositories.ShipmentRepository;
import ua.moisak.PostService.util.LocalDataTimeUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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

    public Shipment findById(Integer id){
        return shipmentRepository.findById(id).orElse(null);
    }

    public List<Shipment> findAll() {
        return  shipmentRepository.findAll();
    }

    public List<Shipment> findAllInDescOrder() {
        return shipmentRepository.findAllInDecOrder();
    }

    public List<Shipment> findAllInDecOrderForStatusPending() {
        return shipmentRepository.findAllInDecOrderForStatusPending();
    }

    public List<Shipment> findAllInDescOrderForCurrentPerson(Integer id) {
        return shipmentRepository.findAllInDecOrderForCurrentPerson(id);
    }

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

    public void save(Shipment shipment) {
        shipmentRepository.save(shipment);
    }

    public void updateEmail(Person person) {
        // update corresponding shipment senderEmail
        shipmentRepository.updateSenderEmail(person.getUsername(), person.getId());
    }

    public void deleteById(Integer id){
        shipmentRepository.deleteById(id);
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

    public Shipment setAdditionFields(Shipment shipment, MultipartFile photo) throws IOException {
        shipment.setLocalDateTime(LocalDataTimeUtil.getLocalDateTimeWithFormatter());
        String[] stringsFonUnicueNumber = {
                shipment.getSenderFullName(),
                shipment.getSenderEmail(),
                shipment.getSenderPhone(),
                shipment.getOrigin(),

                shipment.getRecipientFullName(),
                shipment.getRecipientEmail(),
                shipment.getRecipientPhone(),
                shipment.getDestination(),
                shipment.getShipmentPhoto(),
                shipment.getLocalDateTime(),
                String.valueOf(new Random().nextInt(1000))
        };

        String inv = generateUniqueNumber(stringsFonUnicueNumber);
        shipment.setInvoice(inv);
        shipment.setStatus(ShipmentStatus.PENDING);
        shipment.setWeightVolumetric(shipment.calculateWeightVolumetric());

        //compress photo and set
        shipment.setShipmentPhoto(Base64.getEncoder().encodeToString(photo.getBytes()));

        Person person = personDetailsService.getCurrentUser();
        shipment.setPerson(person);

        return shipment;
    }

}
