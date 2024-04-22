package com.thewinningteam.pms.Service.ServiceImpl;

import com.thewinningteam.pms.DTO.AppointmentDTO;
import com.thewinningteam.pms.DTO.AppointmentRequestDTO;
import com.thewinningteam.pms.Repository.AppointmentRepository;
import com.thewinningteam.pms.Repository.CustomerRepository;
import com.thewinningteam.pms.Repository.ServiceRepository;
import com.thewinningteam.pms.Service.AppointmentService;
import com.thewinningteam.pms.model.Appointment;
import com.thewinningteam.pms.model.Customer;
import com.thewinningteam.pms.model.ServiceProvider;
import com.thewinningteam.pms.model.ServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final ServiceRepository serviceRepository;
    private final CustomerRepository customerRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public void setAppointmentForConnectedCustomer(Authentication connectedUser, Long serviceRequestId, AppointmentRequestDTO appointmentRequest) {
        if (connectedUser == null || !connectedUser.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        // Extract customerId from authentication
        Long customerId = extractCustomerId(connectedUser);

        // Retrieve the corresponding service request entity
        ServiceRequest serviceRequest = serviceRepository.findById(serviceRequestId)
                .orElseThrow(() -> new IllegalArgumentException("ServiceRequest not found with id: " + serviceRequestId));

        // Retrieve the connected customer entity
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + customerId));

        // Retrieve the service provider associated with the service request
        ServiceProvider serviceProvider = serviceRequest.getServiceProvider();

        // Create a new appointment entity
        Appointment appointment = new Appointment();
        appointment.setRequestDate(new Timestamp(System.currentTimeMillis()));
        appointment.setAppointmentDate(appointmentRequest.getAppointmentDate());
        appointment.setMessage(appointmentRequest.getMessage());
        appointment.setCustomer(customer);
        appointment.setServiceProvider(serviceProvider);
        appointment.setService(serviceRequest);

        // Save the appointment entity
        appointmentRepository.save(appointment);
    }

    @Override
    public List<AppointmentDTO> findAllAppointmentsForConnectedCustomer(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        Long customerId = extractCustomerId(authentication);

        return appointmentRepository.findAllByCustomerId(customerId);
    }


    // Helper method to extract customerId from Authentication
    private Long extractCustomerId(Authentication authentication) {
        Customer customer = (Customer) authentication.getPrincipal();
        return customer.getUserId();
    }
}
