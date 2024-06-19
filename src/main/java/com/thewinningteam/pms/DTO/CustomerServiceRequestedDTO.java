package com.thewinningteam.pms.DTO;

import com.thewinningteam.pms.model.ServiceStatus;

import lombok.Data;

import java.sql.Date;


@Data
public class CustomerServiceRequestedDTO {
 private Long id;
 private String firstName;
 private String lastName;
 private ServiceStatus status;
 private byte[] profilePicture;
 private Date appointmentDate;
 private boolean completed;


 public CustomerServiceRequestedDTO(Long id, String firstName, String lastName, ServiceStatus status, byte[] profilePicture, Date appointmentDate, boolean completed) {
  this.id = id;
  this.firstName = firstName;
  this.lastName = lastName;
  this.status = status;
  this.profilePicture = profilePicture;
  this.appointmentDate = appointmentDate;
  this.completed = completed;
 }
}

