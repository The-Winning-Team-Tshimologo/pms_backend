package com.thewinningteam.pms.Service;

import com.thewinningteam.pms.DTO.AppointmentDTO;
import com.thewinningteam.pms.DTO.AppointmentRequestDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.Authentication;

import java.sql.Date;
import java.util.List;

public interface AppointmentService {
   void setAppointmentForConnectedCustomer(Authentication connectedUser, Long serviceRequestId, AppointmentRequestDTO appointmentRequest);
   List<AppointmentDTO> findAllAppointmentsForConnectedCustomer(Authentication authentication);
}
