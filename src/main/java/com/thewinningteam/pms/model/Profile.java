package com.thewinningteam.pms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profile  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @Column(length = 700)
    private String skills;

    @Column(length = 700)
    private String expertise;

    @Column(length = 4000)
    private String professionalSummary;


    @OneToMany(mappedBy = "profile")
    private List<WorkExperience> workExperienceList;

    private Integer numberOfTasksCompleted;

    @OneToMany(mappedBy = "profile")
    private List<Education> education;


    @Column(name = "number_of_years_worked")
    private Integer numberOfYearsWorked;

    @Column(name = "verification")
    private Boolean verification;

    @Column(name = "hourly_rate")
    private Double hourlyRate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ServiceProvider serviceProvider;

    @Override
    public String toString() {
        return "Profile{" +
                "profileId=" + profileId +
                ", skills='" + skills + '\'' +
                ", expertise='" + expertise + '\'' +
                ", professionalSummary='" + professionalSummary + '\'' +
                ", numberOfTasksCompleted=" + numberOfTasksCompleted +
                ", numberOfYearsWorked=" + numberOfYearsWorked +
                ", verification=" + verification +
                ", hourlyRate=" + hourlyRate +
                '}';
    }
}
