package com.thewinningteam.pms.DTO;

import lombok.Data;

import java.sql.Date;

@Data
public class AppointmentRequestDTO {
    private Date appointmentDate;
    private String message;

    // Getters and setters
}
