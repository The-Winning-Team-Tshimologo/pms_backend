package com.thewinningteam.pms.Service;

import com.thewinningteam.pms.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();
    Optional<Category> getCategoryById(Long id);
    Category saveCategory(Category category);
    void deleteCategoryById(Long id);
}
