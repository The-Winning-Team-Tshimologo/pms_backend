package com.thewinningteam.pms.DTO;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class CategoryDTO {
    private Long categoryId;
    private String name;
}
