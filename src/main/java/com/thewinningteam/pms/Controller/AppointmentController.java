package com.thewinningteam.pms.Controller;

import com.thewinningteam.pms.DTO.AppointmentDTO;
import com.thewinningteam.pms.DTO.AppointmentRequestDTO;
import com.thewinningteam.pms.Service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping("/set/{serviceRequestId}")
    public ResponseEntity<String> setAppointmentForConnectedCustomer(
            Authentication authentication,
            @PathVariable Long serviceRequestId,
            @RequestBody AppointmentRequestDTO appointmentRequest) {
        try {
            appointmentService.setAppointmentForConnectedCustomer(authentication, serviceRequestId, appointmentRequest);
            return ResponseEntity.ok("Appointment set successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/all")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointmentsForConnectedCustomer(Authentication authentication) {
        try {
            List<AppointmentDTO> appointments = appointmentService.findAllAppointmentsForConnectedCustomer(authentication);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}

