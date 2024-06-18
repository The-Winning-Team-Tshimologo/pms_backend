package com.thewinningteam.pms.DTO;

import com.thewinningteam.pms.model.Address;
import com.thewinningteam.pms.model.Category;
import com.thewinningteam.pms.model.Customer;
import com.thewinningteam.pms.model.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequestWithAppointmentDTO {
    private Long serviceId;
    private byte[] pictures;
    private Address address;
    private String description;
    private ServiceStatus status;
    private Customer customer;
    private Category category;
    private Date appointmentDate;
    private boolean completed;



}
