
package com.thewinningteam.pms.Repository;

import com.thewinningteam.pms.model.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestimonialRepository extends JpaRepository<Testimonial,Long> {
}