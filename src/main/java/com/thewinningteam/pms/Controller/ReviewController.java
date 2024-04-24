package com.thewinningteam.pms.Controller;

import com.thewinningteam.pms.DTO.ReviewRequestDTO;
import com.thewinningteam.pms.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/service-provider/{serviceRequestId}")
    public ResponseEntity<String> reviewServiceProvider(@PathVariable Long serviceRequestId, @RequestBody ReviewRequestDTO reviewRequestDTO, Authentication authentication) {
        try {
            // Call the service method to review the service provider
            reviewService.reviewServiceProvider(reviewRequestDTO, authentication, serviceRequestId);

            return ResponseEntity.ok("Review submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to submit review: " + e.getMessage());
        }
    }


}
