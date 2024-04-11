package com.thewinningteam.pms.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressDTO {
    private Long addressId;
    private Integer zipCode;
    private String province;
    private String city;
    private String streetName;
}
