package com.thewinningteam.pms.Repository;

import com.thewinningteam.pms.model.Customer;
import com.thewinningteam.pms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
   Optional<Customer> findByEmail(String email);
}