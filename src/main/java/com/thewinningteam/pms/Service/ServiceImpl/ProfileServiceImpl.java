package com.thewinningteam.pms.Service.ServiceImpl;

import com.thewinningteam.pms.Repository.ProfileRepository;
import com.thewinningteam.pms.Service.ProfileService;
import com.thewinningteam.pms.model.Profile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Profile getProfileByServiceProviderId(Authentication authentication, Long serviceProviderId) {
        // Check if the authentication principal is a connected customer
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
//            throw new AccessDeniedException("User is not a connected customer");
//        }
//
//        // Retrieve the profile by service provider ID
//        Optional<Profile> profileOptional = profileRepository.findByServiceProviderId(serviceProviderId);
//        return profileOptional.orElse(null);

        return null;
    }
}

