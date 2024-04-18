package com.thewinningteam.pms.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO {
    private Long userId;
    private String userName;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String mobile;
    private int rating;
    private RoleDTO role;
   // private Set<String> role;
    private AddressDTO address;
    private byte[]  profilePicture;
}
