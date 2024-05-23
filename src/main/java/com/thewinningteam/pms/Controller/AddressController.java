package com.thewinningteam.pms.Controller;

import com.thewinningteam.pms.DTO.AddressDTO;
import com.thewinningteam.pms.Service.ServiceImpl.AddressServiceImpl;
import com.thewinningteam.pms.model.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressServiceImpl addressService;


    @PutMapping("save")
    public ResponseEntity<Address> updateAddress(@RequestBody AddressDTO addressDTO) {
        try {
            Address updatedAddress = addressService.updateAddress(addressDTO);
            return ResponseEntity.ok(updatedAddress);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Address> getAddress(Authentication authentication) {
        try {
            Address address = addressService.getAddress(authentication);
            return ResponseEntity.ok(address);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}