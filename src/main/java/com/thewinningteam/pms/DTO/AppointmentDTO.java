package com.thewinningteam.pms.DTO;

import com.thewinningteam.pms.model.ServiceStatus;
import lombok.Data;

import java.sql.Date;


@Data
public class AppointmentDTO {
    private Date appointmentDate;
    private String serviceProviderFirstName;
    private String serviceProviderLastName;
    private ServiceStatus serviceStatus;

    public AppointmentDTO(Date appointmentDate, String serviceProviderFirstName, String serviceProviderLastName, ServiceStatus serviceStatus) {
        this.appointmentDate = appointmentDate;
        this.serviceProviderFirstName = serviceProviderFirstName;
        this.serviceProviderLastName = serviceProviderLastName;
        this.serviceStatus = serviceStatus;
    }


    // Getters and setters
}


