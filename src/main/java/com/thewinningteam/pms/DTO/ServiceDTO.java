package com.thewinningteam.pms.DTO;

import com.thewinningteam.pms.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder

public class ServiceDTO {
    private Long serviceId;
    private byte[] pictures;
    private Address address;
    private String description;
    private ServiceStatus status;
    private Customer customer;
    private Category category;

    public ServiceDTO(Long serviceId, byte[] pictures, Address address, String description, ServiceStatus status, Customer customer, Category category) {
        this.serviceId = serviceId;
        this.pictures = pictures;
        this.address = address;
        this.description = description;
        this.status = status;
        this.customer = customer;
        this.category = category;
    }

    public ServiceDTO() {

    }
}
