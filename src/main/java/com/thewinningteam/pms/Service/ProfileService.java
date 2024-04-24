package com.thewinningteam.pms.Service;

import com.thewinningteam.pms.model.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
public interface ProfileService {
    Profile getProfileByServiceProviderId(Authentication authentication, Long serviceProviderId);
}

