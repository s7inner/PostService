package ua.moisak.PostService.controllers.rest;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.moisak.PostService.dto.EmployerProfileDTO;
import ua.moisak.PostService.dto.ShipmentDTO;
import ua.moisak.PostService.enums.PerformerProfileStatus;
import ua.moisak.PostService.enums.ShipmentStatus;
import ua.moisak.PostService.models.Person;
import ua.moisak.PostService.models.Profile;
import ua.moisak.PostService.models.Shipment;
import ua.moisak.PostService.services.PersonDetailsService;
import ua.moisak.PostService.services.ShipmentService;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
@RestController // @Controller + @ResponseBody
@RequestMapping("/rest/shipments")
public class RestShipmentController {
    private final ShipmentService shipmentService;
    private final PersonDetailsService personDetailsService;

    private final ModelMapper modelMapper;


    public RestShipmentController(ShipmentService shipmentService, PersonDetailsService personDetailsService, ModelMapper modelMapper) {
        this.shipmentService = shipmentService;
        this.personDetailsService = personDetailsService;
        this.modelMapper = modelMapper;
    }

    //FOR ADMIN-----------------------------------------------------------
    @PreAuthorize("hasAnyRole('ADMIN', 'ONLY_FOR_VIEW')")
    @GetMapping("/admin/list")
    public ResponseEntity<List<ShipmentDTO>> getAllShipmentsForAdmin() {
        List<Shipment> shipments = shipmentService.findAllInDecOrder();
        if (shipments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<ShipmentDTO> shipmentDTOS = shipments.stream()
                .map(s -> modelMapper.map(s, ShipmentDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(shipmentDTOS);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ONLY_FOR_VIEW')")
    @GetMapping("/admin/{id}")
    public ResponseEntity<ShipmentDTO> getShipmentByIdForAdmin(@PathVariable Integer id) {
        Shipment shipment = shipmentService.findById(id);
        if (shipment == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(modelMapper.map(shipment, ShipmentDTO.class), HttpStatus.OK);
    }

    //FOR EMPLOYER---------------------------------------------------------------------------------------
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ONLY_FOR_VIEW')")
    @GetMapping("/employer/list")
    public ResponseEntity<List<ShipmentDTO>> getAllShipmentsForEmployer(Principal principal) {
        List<Shipment> shipments = shipmentService.findAllInDescOrderForCurrentPerson(personDetailsService.findByUsername(principal.getName()).getId());

        if (shipments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<ShipmentDTO> shipmentDTOS = shipments.stream()
                .map(s -> modelMapper.map(s, ShipmentDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(shipmentDTOS);
    }

    @PreAuthorize("hasAnyRole('EMPLOYER', 'ONLY_FOR_VIEW')")
    @GetMapping("/employer/{id}")
    public ResponseEntity<ShipmentDTO> getShipmentByIdForEmployer(@PathVariable Integer id) {
        Shipment shipment = shipmentService.findById(id);
        if (shipment == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(modelMapper.map(shipment, ShipmentDTO.class), HttpStatus.OK);
    }

    @PostMapping("/employer/new")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ONLY_FOR_VIEW')")
    public ResponseEntity<String> createShipment(@RequestBody ShipmentDTO shipmentDTO) throws IOException {
        Shipment shipment = new Shipment();

        shipment = modelMapper.map(shipmentDTO, Shipment.class);
        shipment.setId(0);//якщо user, поставить id своє
        shipment = shipmentService.setAdditionFieldsREST(shipment);//without photo, photo = null

        shipmentService.save(shipment);
        return ResponseEntity.ok("Shipment successfully created!");

    }
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ONLY_FOR_VIEW')")
    @PutMapping("/employer/update/{id}")
    public ResponseEntity<String> updateProfileEmployer(@RequestBody ShipmentDTO shipmentDTO,@PathVariable Integer id) throws IOException {
        Shipment shipment = shipmentService.findById(id);
        if (shipment==null) {
            return ResponseEntity.badRequest().body("The user does not have a shipment with this ID!");
        }

        shipment = modelMapper.map(shipmentDTO, Shipment.class);
        shipment.setId(id);//якщо user, поставить id своє
        shipment = shipmentService.setAdditionFieldsREST(shipment);//without photo, photo = null

        shipmentService.save(shipment);
        return ResponseEntity.ok("Shipment successfully updated!");
    }

    @DeleteMapping("/employer/delete/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ONLY_FOR_VIEW')")
    public ResponseEntity<String> deleteShipment(@PathVariable Integer id) {
        Shipment shipment = shipmentService.findById(id);
        if (shipment==null) {
            return ResponseEntity.badRequest().body("The user does not have a shipment with this ID!");
        }
        shipmentService.deleteById(id);
        return ResponseEntity.ok("Shipment successfully deleted!");

    }
    //ROLE PERFORMER--------------------------------------------------------------------------------------------
    @PreAuthorize("hasAnyRole('PERFORMER', 'ONLY_FOR_VIEW')")
    @GetMapping("/performer/list")
    public ResponseEntity<List<ShipmentDTO>> getAllShipmentsForPerformer(Principal principal) {
        List<Shipment> shipments = shipmentService.findAllByIdForPerformer(personDetailsService.findByUsername(principal.getName()).getId());

        if (shipments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<ShipmentDTO> shipmentDTOS = shipments.stream()
                .map(s -> modelMapper.map(s, ShipmentDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(shipmentDTOS);
    }


    @PreAuthorize("hasAnyRole('PERFORMER', 'ONLY_FOR_VIEW')")
    @PatchMapping("/performer/{id}/changeStatus/{status}")
    public ResponseEntity<String> validateProfilePerformer(@PathVariable Integer id,@PathVariable String status) {
        Shipment shipment = shipmentService.findById(id);
        if (shipment==null) {
            return ResponseEntity.badRequest().body("The user does not have a shipment with this ID!");
        }
        shipment.setStatus(ShipmentStatus.valueOf(status));
        shipmentService.save(shipment);
        return ResponseEntity.ok("Status successfully changed!");
    }

}
