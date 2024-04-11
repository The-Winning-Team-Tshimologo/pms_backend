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
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long educationId;
    private String institution;
    private String qualification;
    private Date startDate;
    private Date enddate;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

}
