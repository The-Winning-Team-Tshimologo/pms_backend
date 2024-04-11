package com.thewinningteam.pms.Repository;

import com.thewinningteam.pms.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider,Long> {
    ServiceProvider findByEmailOrUserName(String email, String username);
}