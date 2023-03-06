package ua.moisak.PostService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.models.Shipment;
import ua.moisak.PostService.repositories.ShipmentRepository;
import ua.moisak.PostService.services.PersonDetailsService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shipments")
public class ShipmentController {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private PersonDetailsService personDetailsService;

    // GET all shipments
    @GetMapping("list")
    public String getAllShipments(Model model) {
        List<Shipment> shipments = shipmentRepository.findAll();
        model.addAttribute("shipments", shipments);
        return "shipments/list";
    }

    // Creating new Shipment--------------------------------------------------
    @GetMapping("/new")
    public String showNewShipmentForm(Model model) {
        model.addAttribute("shipment", new Shipment());
        return "/shipments/new";
    }

    @PostMapping("/new")
    public String createNewShipment(@ModelAttribute("shipment") Shipment shipment) {

        String[] stringsFonUnicueNumber = {
                shipment.getSenderFullName(),
                shipment.getSenderEmail(),
                shipment.getSenderPhone(),
                shipment.getOrigin(),

                shipment.getRecipientFullName(),
                shipment.getRecipientEmail(),
                shipment.getRecipientPhone(),
                shipment.getDestination(),
        };

        String inv = generateUniqueNumber(stringsFonUnicueNumber);

        Optional<Person> author = personDetailsService.getCurrentUser(); // get the currently authenticated user
        shipment.setPerson(author.get()); // set the author of the message
        shipment.setInvoice(inv);
        shipmentRepository.save(shipment); // save the message to the database
        return "redirect:/shipments/list"; // redirect to the newly created message page
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
        byte[] first10Bytes = Arrays.copyOf(hashedBytes, 10);
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : first10Bytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }
        String uniqueNumber = "IN"+ hexStringBuilder.toString().toUpperCase();

        return uniqueNumber;
    }

    //-----------------------------------------------------------------------

//    @RequestMapping(value = "/shipment" ,method = RequestMethod.GET, headers = "Accept=application/json", produces =  "application/json" )

    @GetMapping("/shipment")
    public String shipmentnPage(@ModelAttribute("shipment") Shipment person) {
        return "shipments/shipment";
    }
//    @RequestMapping(value = "/shipment" ,method = RequestMethod.POST, headers = "Accept=application/json", produces =  "application/json" )

    @PostMapping("/shipment")
    public String saveShipment(@ModelAttribute("shipment") Shipment shipment) {
        // Save the shipment object to the database using Hibernate
        shipmentRepository.save(shipment);

        // Redirect to the shipment list page
        return "redirect:/shipments";
    }


    // GET a shipment by id
    @GetMapping("/{id}")
    public String getShipmentById(@PathVariable Integer id, Model model) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(null);
        model.addAttribute("shipment", shipment);
        return "shipments/view";
    }

    // POST a new shipment
    @PostMapping("")
    public String addShipment(@ModelAttribute("shipment") Shipment shipment) {
        shipmentRepository.save(shipment);
        return "redirect:/shipments";
    }

    // PUT (update) a shipment
    @PutMapping("/{id}")
    public String updateShipment(@PathVariable Integer id, @ModelAttribute("shipment") Shipment updatedShipment) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(null);
        shipment.setInvoice(updatedShipment.getInvoice());
        shipment.setSenderFullName(updatedShipment.getSenderFullName());
        shipment.setSenderEmail(updatedShipment.getSenderEmail());
        shipment.setSenderPhone(updatedShipment.getSenderPhone());
        // set other fields

        shipmentRepository.save(shipment);
        return "redirect:/shipments/" + id;
    }

    // DELETE a shipment
    @DeleteMapping("/{id}")
    public String deleteShipment(@PathVariable Integer id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(null);
        shipmentRepository.delete(shipment);
        return "redirect:/shipments";
    }
}
