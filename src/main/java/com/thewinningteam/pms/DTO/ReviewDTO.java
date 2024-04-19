package com.thewinningteam.pms.DTO;

import com.thewinningteam.pms.model.Customer;
import com.thewinningteam.pms.model.ServiceProvider;
import com.thewinningteam.pms.model.ServiceRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class ReviewDTO {
    private Long reviewId;
    private String feedback;
    private int rating;
    private Customer customer;
    private ServiceRequest service;
    private ServiceProvider serviceProvider;
}
