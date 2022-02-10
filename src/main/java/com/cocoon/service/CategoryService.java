package com.cocoon.service;

import com.cocoon.dto.CategoryDTO;
import com.cocoon.exception.CocoonException;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    void save(CategoryDTO categoryDTO) throws CocoonException;
}
