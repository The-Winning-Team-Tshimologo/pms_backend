package com.thewinningteam.pms.DTO;

import lombok.Data;
import java.util.Date;

@Data
public class CustomerServiceDTO {
    private Date appointmentDate;
    private String serviceProviderName;
}

