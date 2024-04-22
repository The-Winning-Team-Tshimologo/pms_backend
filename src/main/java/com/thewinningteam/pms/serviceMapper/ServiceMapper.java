package com.thewinningteam.pms.serviceMapper;

import com.thewinningteam.pms.DTO.ServiceDTO;
import com.thewinningteam.pms.model.ServiceRequest;
import org.springframework.stereotype.Service;

@Service
public class ServiceMapper {

    public ServiceDTO toDTO(ServiceRequest request) {
        // Map the fields from ServiceRequest to ServiceDTO
        return ServiceDTO.builder()
                .serviceId(request.getServiceId())
                .address(request.getAddress())
                .customer(request.getCustomer())
                .category(request.getCategory())
                .description(request.getDescription())
                .build();
    }
}

