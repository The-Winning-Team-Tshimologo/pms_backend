package com.thewinningteam.pms.Service;

import com.thewinningteam.pms.DTO.AddressDTO;
import com.thewinningteam.pms.model.Address;
import org.springframework.security.core.Authentication;

public interface AddressService {
    public Address updateAddress( AddressDTO addressDTO);

    public Address  getAddress(Authentication authentication);
}
