package com.thewinningteam.pms.Service;

import com.thewinningteam.pms.DTO.CustomerDTO;
import com.thewinningteam.pms.DTO.LoginDTO;
import com.thewinningteam.pms.model.Customer;

import java.util.Optional;

public interface CustomerService {
    public Customer SaveCustomer(Customer customer);

    public Optional<CustomerDTO> GetCustomerById(Long userID);
    public void DeleteCustomerById(Long id);
    public Optional<CustomerDTO> updateCustomer(Customer updatedCustomer);

}
