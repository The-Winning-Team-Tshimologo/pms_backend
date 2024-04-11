package com.thewinningteam.pms.Repository;

import com.thewinningteam.pms.model.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperience,Long> {
}