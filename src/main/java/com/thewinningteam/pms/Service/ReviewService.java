package com.thewinningteam.pms.Service;

import com.thewinningteam.pms.DTO.ReviewRequestDTO;
import com.thewinningteam.pms.model.ServiceRequest;
import org.springframework.security.core.Authentication;

public interface ReviewService {
    void reviewServiceProvider(ReviewRequestDTO reviewRequestDTO, Authentication authentication, Long serviceRequestId) ;
}
