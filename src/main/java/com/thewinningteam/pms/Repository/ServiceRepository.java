
package com.thewinningteam.pms.Repository;


import com.thewinningteam.pms.DTO.RequestSystemWideDTO;
import com.thewinningteam.pms.DTO.ServiceDTO;
import com.thewinningteam.pms.model.Category;

import com.thewinningteam.pms.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
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

    @Query("SELECT NEW com.thewinningteam.pms.DTO.RequestSystemWideDTO(s.customer.profilePicture, s.address.streetName, s.address.city, SIZE(s.customer.reviews), s.customer.firstName, s.customer.lastName, s.category.name) FROM ServiceRequest s WHERE s.serviceProvider.userId IS NULL")
    List<RequestSystemWideDTO> findAllWithoutServiceProvider();






}

