package com.thewinningteam.pms.Repository;

import com.thewinningteam.pms.DTO.AppointmentDTO;
import com.thewinningteam.pms.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {

    @Query("SELECT NEW com.thewinningteam.pms.DTO.AppointmentDTO(a.appointmentDate, sp.firstName, sp.lastName, sr.status) " +
            "FROM Appointment a " +
            "JOIN a.serviceProvider sp " +
            "JOIN a.service sr " +
            "WHERE a.customer.userId = :customerId")
    List<AppointmentDTO> findAllByCustomerId(Long customerId);


    List<Appointment> findAllByCustomerUserId(Long customerId);
}

