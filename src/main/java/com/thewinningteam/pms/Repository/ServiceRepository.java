
package com.thewinningteam.pms.Repository;

import com.thewinningteam.pms.model.Category;

import com.thewinningteam.pms.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceRequest,Long> {
    @Repository
    public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
        List<ServiceRequest> findByCategory(Category category);
    }

}