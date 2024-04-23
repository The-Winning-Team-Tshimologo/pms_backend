package com.thewinningteam.pms.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrowseServiceProviderDTO {
    private byte[] pictures;
    private int rating;
    private String firstName;
    private AddressDTO address;
    private String lastName;
    private String category;
}
