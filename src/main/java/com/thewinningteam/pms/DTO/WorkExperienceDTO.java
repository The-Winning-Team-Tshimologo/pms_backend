package com.thewinningteam.pms.DTO;

import com.thewinningteam.pms.model.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@RequiredArgsConstructor
@Data
public class WorkExperienceDTO {
    private Long workId;
    private Date startDate;
    private Date endDate;
    private String title;
    private String companyName;
    private String description;
//    private Profile profile;
}
