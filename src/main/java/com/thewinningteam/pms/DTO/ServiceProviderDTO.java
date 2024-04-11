package com.thewinningteam.pms.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceProviderDTO {
    private Long userId;
    private String userName;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String mobile;
    private int rating;
    private RoleDTO role;
    private AddressDTO address;
    private byte[]  profilePicture;
}
