
package com.thewinningteam.pms.Repository;


import com.thewinningteam.pms.DTO.CustomerServiceRequestedDTO;
import com.thewinningteam.pms.DTO.RequestSystemWideDTO;
import com.thewinningteam.pms.DTO.ServiceDTO;
import com.thewinningteam.pms.model.Category;

import com.thewinningteam.pms.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceRequest,Long> {
    List<ServiceRequest> findByCategory(Category category);


    @Query("SELECT new com.thewinningteam.pms.DTO.ServiceDTO(sr.serviceId, sr.pictures, sr.address, sr.description, sr.status, sr.customer, sr.category) " +
            "FROM ServiceRequest sr " +
            "JOIN sr.customer c " +
            "WHERE sr.serviceProvider.userId = :serviceProviderId")
    List<ServiceDTO> findServiceRequestsWithCustomerByServiceProviderId(Long serviceProviderId);

    @Query("SELECT NEW com.thewinningteam.pms.DTO.RequestSystemWideDTO(s.customer.userId, s.customer.userName, s.serviceId, s.customer.profilePicture, s.address.streetName, s.address.city, SIZE(s.customer.reviews), s.customer.firstName, s.customer.lastName, s.category.name) FROM ServiceRequest s WHERE s.serviceProvider.userId IS NULL")
    List<RequestSystemWideDTO> findAllWithoutServiceProvider();

    @Query("SELECT NEW com.thewinningteam.pms.DTO.CustomerServiceRequestedDTO(sp.firstName, sp.lastName, s.status, sp.profilePicture, a.appointmentDate) " +
            "FROM ServiceRequest s " +
            "JOIN s.serviceProvider sp " +
            "JOIN Appointment a ON a.service = s " +
            "WHERE s.customer.userId = :customerId")
    List<CustomerServiceRequestedDTO> getCustomerServiceRequestedDTOByCustomerId( Long customerId);

    @Query("SELECT COUNT(sr) FROM ServiceRequest sr")
    long countAllServiceRequests();

    @Modifying
    @Query("UPDATE ServiceRequest s SET s.serviceProvider.userId = :providerId WHERE s.serviceId = :serviceId AND s.customer.userId = :customerId")
    void assignServiceProviderToCustomerService( Long serviceId, Long providerId, Long customerId);


    ServiceRequest findByServiceIdAndServiceProviderUserId(Long serviceId, Long serviceProviderId);
}








