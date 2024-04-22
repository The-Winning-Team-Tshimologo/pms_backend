package com.thewinningteam.pms.DTO;

import lombok.Data;

@Data
public class ServiceRequestDTO {
    private Long id;
    private String serviceName;
    private String customerName;

    // Getters and setters
}
