package com.thewinningteam.pms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore

    private List<User> users;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;


}

