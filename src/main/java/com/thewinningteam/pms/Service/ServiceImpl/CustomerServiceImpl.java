package com.thewinningteam.pms.Service.ServiceImpl;


import com.thewinningteam.pms.DTO.CustomerDTO;
import com.thewinningteam.pms.DTO.LoginDTO;
import com.thewinningteam.pms.Repository.AddressRepository;
import com.thewinningteam.pms.Repository.CustomerRepository;
import com.thewinningteam.pms.Repository.RoleRepository;
import com.thewinningteam.pms.Service.CustomerService;
import com.thewinningteam.pms.model.Address;
import com.thewinningteam.pms.model.Customer;
import com.thewinningteam.pms.model.ERole;
import com.thewinningteam.pms.model.Role;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;
    private RoleRepository roleRepository;
    private ModelMapper modelMapper;
    private AddressRepository addressRepository;


    @Override
    public Customer SaveCustomer(Customer customer) {
        Customer existingCustomer = customerRepository.findByEmailOrUserName(customer.getEmail(), customer.getUsername());
        if (existingCustomer != null) {
            throw new IllegalArgumentException("A customer with the same email or username already exists.");
        }
        Address address = customer.getAddress();
        address = addressRepository.save(address);

        customer.setAddress(address);

//        Long roleID = 1L;
//        Role role = roleRepository.findById(roleID).orElseThrow(() -> new IllegalArgumentException("Role not found."));
//        System.out.println("Retrieved Role: " + role);

        Role role = roleRepository.findByName(ERole.ROLE_CUSTOMER).orElseThrow(() -> new IllegalArgumentException("Role not found."));

        List<Role> roles = new ArrayList<>();
        roles.add(role);
        customer.setRoles(roles);
        System.out.println();
        return customerRepository.save(customer);
//        return customer;
    }

    @Override
    public String Login(LoginDTO loginDTO) {
        Customer customer = customerRepository.findByEmailOrUserName(loginDTO.getUsernameOrEmail(), loginDTO.getUsernameOrEmail());
        if (customer != null && loginDTO.getPassword().equals(customer.getPassword())) {
            return "Login successful";
        } else {
            return "Invalid username/email or password";
        }
    }


    @Override
    public Optional<CustomerDTO> GetCustomerById(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        return customerOptional.map(customer -> modelMapper.map(customer, CustomerDTO.class));
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
