package com.thewinningteam.pms.Repository;

import com.thewinningteam.pms.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {


    Customer findByEmailOrUserName(String email, String userName);
}

