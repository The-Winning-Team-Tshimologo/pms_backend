package com.thewinningteam.pms.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrowseServiceProviderDTO {
    private Long userId;
    private String userName;
    private byte[] pictures;
    private double rating;
    private String firstName;
    private AddressDTO address;
    private String lastName;
    private String category;
}
