package com.thewinningteam.pms.DTO;

import com.thewinningteam.pms.model.Education;
import com.thewinningteam.pms.model.WorkExperience;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;

import java.util.List;

public class ProfileDTO {
    private Long profileId;
    private String skills;
    private String expertise;
    private String professionalSummary;
    private List<WorkExperienceDTO> workExperienceList;
    private Integer numberOfTasksCompleted;
    private List<EducationDTO> education;
    private Integer numberOfYearsWorked;
    private Boolean verification;
    private Double hourlyRate;
}
