package com.thewinningteam.pms.mapper;

import com.thewinningteam.pms.DTO.ServiceDTO;
import org.springframework.stereotype.Service;

@Service
public class ServiceMapper {

    public static ServiceDTO toDTO(ServiceDTO serviceRequest) {
        ServiceDTO dto = new ServiceDTO();
        dto.setServiceId(serviceRequest.getServiceId());
        dto.setPictures(serviceRequest.getPictures());
        dto.setAddress(serviceRequest.getAddress());
        dto.setDescription(serviceRequest.getDescription());
        dto.setStatus(serviceRequest.getStatus());
        dto.setCustomer(serviceRequest.getCustomer());
        dto.setCategory(serviceRequest.getCategory());
        return dto;
    }
}


