package com.thewinningteam.pms.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceAndAppointmentDTO {
    private CreateServiceRequestDTO createServiceRequestDTO;
    private CreateAppointmentDTO createAppointmentDTO;

    // Getters and setters
}
