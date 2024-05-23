package com.thewinningteam.pms.Service.ServiceImpl;

import com.thewinningteam.pms.DTO.AddressDTO;
import com.thewinningteam.pms.Repository.AddressRepository;
import com.thewinningteam.pms.Repository.UserRepository;
import com.thewinningteam.pms.Service.AddressService;
import com.thewinningteam.pms.model.Address;
import com.thewinningteam.pms.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final UserRepository userRepository;

    private final AddressRepository addressRepository;
    @Override
    public Address updateAddress(AddressDTO addressDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        User userDetails = (User) auth.getPrincipal();
        Long userId = userDetails.getUserId();

        // Fetch the user from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Now you have the authenticated user's details, you can proceed with updating the address
        // For example:
        Address currentAddress = user.getAddress();
        currentAddress.setZipCode(addressDTO.getZipCode());
        currentAddress.setProvince(addressDTO.getProvince());
        currentAddress.setCity(addressDTO.getCity());
        currentAddress.setStreetName(addressDTO.getStreetName());

        return addressRepository.save(currentAddress);
    }

    @Override
    public Address getAddress(Authentication authentication) {

        User userDetails = (User) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        // Fetch the user from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user != null) {
            return user.getAddress(); // Assuming getAddress() returns the user's address
        } else {
            throw new RuntimeException("User not found");
        }
    }


}
