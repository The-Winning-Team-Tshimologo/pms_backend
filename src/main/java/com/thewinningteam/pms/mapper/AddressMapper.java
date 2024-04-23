package com.thewinningteam.pms.mapper;

import com.thewinningteam.pms.DTO.AddressDTO;
import com.thewinningteam.pms.model.Address;
import org.springframework.stereotype.Service;

@Service
public class AddressMapper {

    public AddressDTO toDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setStreetName(address.getStreetName());
        dto.setCity(address.getCity());
        // Map other address properties as needed
        return dto;
    }

    public Address toEntity(AddressDTO dto) {
        Address address = new Address();
        address.setStreetName(dto.getStreetName());
        address.setCity(dto.getCity());
        // Map other address properties as needed
        return address;
    }
}
