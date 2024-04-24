package com.thewinningteam.pms.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDTO {
    @NotNull(message = "204")
    private Long serviceRequestId;

    @Positive(message = "200")
    @Min(value = 0, message = "201")
    @Max(value = 5, message = "202")
    private double rating;

    @NotNull(message = "203")
    @NotEmpty(message = "203")
    @NotBlank(message = "203")
    private String feedback;
}
