
package com.thewinningteam.pms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_id")
    private Long workId;

    private Date startDate;
    private Date endDate;
    private String title;
    private String companyName;
    private String description;
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

//    @Override
//    public String toString() {
//        return "WorkExperience{" +
//                "workId=" + workId +
//                ", startDate=" + startDate +
//                ", endDate=" + endDate +
//                ", title='" + title + '\'' +
//                ", companyName='" + companyName + '\'' +
//                ", description='" + description + '\'' +
//                '}';
//    }


}

