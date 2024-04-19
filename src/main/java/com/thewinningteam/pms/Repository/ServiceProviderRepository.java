package com.thewinningteam.pms.Repository;

import com.thewinningteam.pms.model.AcceptanceStatus;
import com.thewinningteam.pms.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider,Long> {
    ServiceProvider findByEmailOrUserName(String email, String username);

    @Query("SELECT sp FROM ServiceProvider sp WHERE sp.acceptanceStatus =" +
            " com.thewinningteam.pms.model.AcceptanceStatus.PENDING")
    List<ServiceProvider> findAllPendingServiceProviders();

    @Query("SELECT sp FROM ServiceProvider sp WHERE sp.acceptanceStatus = :status")
    List<ServiceProvider> findByAcceptanceStatus(@Param("status") AcceptanceStatus status);
}