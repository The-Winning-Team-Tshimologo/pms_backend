package com.thewinningteam.pms.Service.ServiceImpl;


import com.thewinningteam.pms.DTO.CustomerDTO;
import com.thewinningteam.pms.DTO.CustomerServiceDTO;
import com.thewinningteam.pms.DTO.LoginDTO;
import com.thewinningteam.pms.DTO.RoleDTO;
import com.thewinningteam.pms.Repository.AddressRepository;
import com.thewinningteam.pms.Repository.AppointmentRepository;
import com.thewinningteam.pms.Repository.CustomerRepository;
import com.thewinningteam.pms.Repository.RoleRepository;
import com.thewinningteam.pms.Service.CustomerService;
import com.thewinningteam.pms.model.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor

public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;
    private RoleRepository roleRepository;
    private ModelMapper modelMapper;
    private AddressRepository addressRepository;
    private PasswordEncoder passwordEncoder;

    private AppointmentRepository appointmentRepository;



    @Override
    public Customer SaveCustomer(Customer customer) {
        Customer existingCustomer = customerRepository.findByEmailOrUserName(customer.getEmail(), customer.getUsername());
        if (existingCustomer != null) {
            throw new IllegalArgumentException("A customer with the same email or username already exists.");
        }

        Address address = customer.getAddress();
        address = addressRepository.save(address);
        customer.setAddress(address);

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        Role role = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                .orElseThrow(() -> new IllegalArgumentException("Role not found."));
        customer.setRoles((Role)role);

        return customerRepository.save(customer);
    }

    @Override
    public List<CustomerServiceDTO> getAllServicesForConnectedCustomer(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        Long customerId = extractCustomerId(authentication);

        return appointmentRepository.findAllByCustomerUserId(customerId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private CustomerServiceDTO mapToDTO(Appointment appointment) {
        CustomerServiceDTO dto = new CustomerServiceDTO();
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setServiceProviderName(appointment.getServiceProvider().getName());
        return dto;
    }

    // Helper method to extract customerId from Authentication
    private Long extractCustomerId(Authentication authentication) {
        Customer customer = (Customer) authentication.getPrincipal();
        return customer.getUserId();
    }





    @Override
    public Optional<CustomerDTO> GetCustomerById(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);

        return customerOptional.map(customer -> {
            CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
            customerDTO.setRole(modelMapper.map(customer.getRoles(), RoleDTO.class));
            return customerDTO;
        });
    }

    @Override
    public void DeleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(Customer updatedCustomer) {
        Long customerId = updatedCustomer.getUserId();

        // Check if the customer exists
        if (!customerRepository.existsById(customerId)) {
            return Optional.empty(); // Customer with given ID doesn't exist
        }

        Customer savedCustomer = customerRepository.save(updatedCustomer);
        return Optional.of(modelMapper.map(savedCustomer, CustomerDTO.class));
    }
}
