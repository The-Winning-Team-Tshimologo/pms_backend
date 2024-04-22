package com.thewinningteam.pms.DTO;

import com.thewinningteam.pms.model.Address;
import lombok.Data;

@Data
public class CreateServiceRequestDTO {
    private String description;
    private Address address;
}
