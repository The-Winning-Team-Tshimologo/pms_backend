package com.thewinningteam.pms.mapper;

import com.thewinningteam.pms.DTO.ReviewRequestDTO;
import com.thewinningteam.pms.model.Customer;
import com.thewinningteam.pms.model.Review;
import com.thewinningteam.pms.model.ServiceRequest;
import org.springframework.stereotype.Service;

@Service
public class ReviewMapper {

    public Review mapToReviewEntity(ReviewRequestDTO reviewRequestDTO, Customer customer, ServiceRequest serviceRequest) {
        Review review = new Review();
        review.setRating(reviewRequestDTO.getRating());
        review.setFeedback(reviewRequestDTO.getFeedback());
        review.setCustomer(customer);
        review.setService(serviceRequest);
        review.setServiceProvider(serviceRequest.getServiceProvider());
        return review;
    }
}

