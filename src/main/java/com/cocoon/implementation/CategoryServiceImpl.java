package com.cocoon.implementation;

import com.cocoon.dto.CategoryDTO;
import com.cocoon.entity.Category;
import com.cocoon.repository.CategoryRepo;
import com.cocoon.service.CategoryService;
import com.cocoon.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{

    private CategoryRepo categoryRepo;
    private MapperUtil mapperUtil;

    public CategoryServiceImpl(CategoryRepo categoryRepo, MapperUtil mapperUtil) {
        this.categoryRepo = categoryRepo;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepo.findAll();
        return categories.stream().map(category -> mapperUtil.convert(category,new CategoryDTO())).collect(Collectors.toList());
    }
}
