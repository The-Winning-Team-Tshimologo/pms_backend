package com.thewinningteam.pms.DTO;

import com.thewinningteam.pms.model.Profile;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@RequiredArgsConstructor
@Data
public class EducationDTO {
    private Long educationId;
    private String institution;
    private String qualification;
    private Date startDate;
    private Date enddate;
//    private Profile profile;
}
