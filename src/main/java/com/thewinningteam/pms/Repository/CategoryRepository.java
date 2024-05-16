package com.thewinningteam.pms.Repository;

import com.thewinningteam.pms.model.Category;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query("SELECT c FROM Category c WHERE c.name = :name")
    Optional<Category> findFirstByName(@Param("name") String name, PageRequest pageable);
}