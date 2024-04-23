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
    private boolean appointmentSet; // Flag indicating if appointment is set

    public AppointmentDTO(Date appointmentDate, String serviceProviderFirstName, String serviceProviderLastName, ServiceStatus serviceStatus, boolean appointmentSet) {
        this.appointmentDate = appointmentDate;
        this.serviceProviderFirstName = serviceProviderFirstName;
        this.serviceProviderLastName = serviceProviderLastName;
        this.serviceStatus = serviceStatus;
        this.appointmentSet = appointmentSet;
    }

    // Getters and setters
}


