package com.thewinningteam.pms.DTO;

import com.thewinningteam.pms.model.ServiceStatus;

import lombok.Data;

import java.sql.Date;


@Data
public class CustomerServiceRequestedDTO {
 private String firstName;
 private String lastName;
 private ServiceStatus status;
 private byte[] profilePicture;
 private Date appointmentDate;

 public CustomerServiceRequestedDTO(String firstName, String lastName, ServiceStatus status, byte[] profilePicture, Date appointmentDate) {
  this.firstName = firstName;
  this.lastName = lastName;
  this.status = status;
  this.profilePicture = profilePicture;
  this.appointmentDate = appointmentDate;
 }
}

