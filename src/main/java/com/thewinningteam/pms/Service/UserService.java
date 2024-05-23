package com.thewinningteam.pms.Service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void saveProfilePicture(Authentication authentication, MultipartFile imageFile);
}
