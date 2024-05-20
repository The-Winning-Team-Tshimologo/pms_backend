package com.thewinningteam.pms.Repository;

import com.thewinningteam.pms.model.Customer;
import com.thewinningteam.pms.model.User;
import org.springframework.beans.PropertyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
   Optional<User> findByEmail(String email);


    User findByEmailOrUserName(String userEmail, String userEmail1);

    Optional<User> findByUserName(String userName);
}