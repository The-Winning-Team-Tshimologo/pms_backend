package com.thewinningteam.pms.Service.ServiceImpl;


import com.thewinningteam.pms.DTO.*;
import com.thewinningteam.pms.Repository.*;
import com.thewinningteam.pms.Service.CustomerService;
import com.thewinningteam.pms.model.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service

@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
   private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;

    private final AppointmentRepository appointmentRepository;



    public void signUpCustomer(CustomerDTO customerDTO) {

        User existingUser = userRepository.findByEmailOrUserName(customerDTO.getEmail(), customerDTO.getUserName());
        if (existingUser != null) {
            throw new IllegalArgumentException("Email or username already exists");
        }

        Customer customer = new Customer();
        customer.setUserName(customerDTO.getUserName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setMobile(customerDTO.getMobile());
        customer.setProfilePicture(customerDTO.getProfilePicture());

        // Convert AddressDTO to Address entity
        AddressDTO addressDTO = customerDTO.getAddress();
        Address address = new Address();
        address.setStreetName(addressDTO.getStreetName());
        address.setCity(addressDTO.getCity());
        address.setProvince(addressDTO.getProvince());
        address.setZipCode(address.getZipCode());
        customer.setAddress(address);

        customer.setEnabled(true); // Enable the user by default

        // Check if the customer role exists, if not, create it
        Optional<Role> optionalRole = roleRepository.findByName(ERole.ROLE_CUSTOMER);
        Role customerRole = optionalRole.orElseGet(() -> {
            Role newRole = new Role();
            newRole.setName(ERole.ROLE_CUSTOMER);
            return roleRepository.save(newRole);
        });

        customer.setRoles(customerRole);

        customerRepository.save(customer);
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
