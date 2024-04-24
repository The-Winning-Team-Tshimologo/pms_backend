package com.thewinningteam.pms.Service.ServiceImpl;

import com.thewinningteam.pms.DTO.ReviewRequestDTO;
import com.thewinningteam.pms.Repository.ReviewRepository;
import com.thewinningteam.pms.Repository.ServiceRepository;
import com.thewinningteam.pms.Service.ReviewService;
import com.thewinningteam.pms.mapper.ReviewMapper;
import com.thewinningteam.pms.model.Customer;
import com.thewinningteam.pms.model.Review;
import com.thewinningteam.pms.model.ServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private  final ServiceRepository serviceRepository;

    private final ReviewMapper reviewMapper;
    @Override
    public void reviewServiceProvider(ReviewRequestDTO reviewRequestDTO, Authentication authentication, Long serviceRequestId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        // Check if the authentication principal is a connected customer
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
            throw new AccessDeniedException("User is not a connected customer");
        }

        // Get the connected customer
        Customer customer = (Customer) userDetails;

        // Retrieve the service request by its ID
        ServiceRequest serviceRequest = serviceRepository.findById(serviceRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Service request not found"));

        // Check if the service request is completed
        if (!serviceRequest.isCompleted()) {
            throw new IllegalArgumentException("Service request must be completed to leave a review.");
        }

        // Map the DTO to a Review entity
        Review review = reviewMapper.mapToReviewEntity(reviewRequestDTO, customer, serviceRequest);

        // Save the review
        reviewRepository.save(review);
    }

}
