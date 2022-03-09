package com.cocoon.implementation;

import com.cocoon.dto.CategoryDTO;
import com.cocoon.dto.CompanyDTO;
import com.cocoon.dto.ProductDTO;
import com.cocoon.entity.Category;
import com.cocoon.entity.Company;
import com.cocoon.entity.Product;
import com.cocoon.entity.common.UserPrincipal;
import com.cocoon.exception.CocoonException;
import com.cocoon.repository.CategoryRepo;
import com.cocoon.repository.ProductRepository;
import com.cocoon.service.CategoryService;
import com.cocoon.service.CompanyService;
import com.cocoon.service.ProductService;
import com.cocoon.util.MapperUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{

    private CategoryRepo categoryRepo;
    private MapperUtil mapperUtil;
    private ProductService productService;
    private CompanyService companyService;

    public CategoryServiceImpl(CategoryRepo categoryRepo, MapperUtil mapperUtil,
                               ProductService productService, CompanyService companyService) {
        this.categoryRepo = categoryRepo;
        this.mapperUtil = mapperUtil;
        this.productService = productService;
        this.companyService = companyService;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepo.findAll();
        return categories.stream().map(category -> mapperUtil.convert(category,new CategoryDTO())).collect(Collectors.toList());
    }

    @Override
    public void save(CategoryDTO categoryDTO) throws CocoonException {
        Category category = mapperUtil.convert(categoryDTO, new Category());
        if (categoryRepo.existsByDescription(category.getDescription()))
        throw new CocoonException("category is already exist");
        category.setEnabled(true);

        category.setCompany(mapperUtil.convert(companyService.getCompanyByLoggedInUser(), new Company()));

        categoryRepo.save(category);
    }

    @Override
    public CategoryDTO getCategoryByDescription(String descrition) throws CocoonException {
        Category category = categoryRepo.getByDescription(descrition);
        if (category==null)
            throw new CocoonException("there is no category which you search");
        CategoryDTO categoryDTO = mapperUtil.convert(category, new CategoryDTO());
        return categoryDTO;
    }

    @Override
    public void update(CategoryDTO categoryDTO) throws CocoonException {
        Category category = categoryRepo.getById(categoryDTO.getId());
        if (category==null)
            throw new CocoonException("there is no cateory");
        category.setDescription(categoryDTO.getDescription());
        categoryRepo.save(category);


    }

    @Override
    public CategoryDTO getById(Long id) {
        Category category = categoryRepo.getById(id);
        CategoryDTO categoryDTO = mapperUtil.convert(category, new CategoryDTO());
        return categoryDTO;
    }

    @Override
    public void delete(CategoryDTO categoryDTO) throws CocoonException {
        Category category = categoryRepo.getCategoryById(categoryDTO.getId());
        if (category==null)
            throw new CocoonException("category does not exist");
        List<ProductDTO> productDTOList = productService.findProductsByCategoryId(categoryDTO.getId());
        if (productDTOList.size()>0)
            throw new CocoonException("category has relation");
        category.setIsDeleted(true);
        categoryRepo.save(category);
    }

    @Override
    public List<CategoryDTO> getCategoryByCompany_Id() {
        List<Category> categories = categoryRepo.getCategoryByCompany_Id(companyService.getCompanyByLoggedInUser().getId());
        return categories.stream().map(category -> mapperUtil.convert(category,new CategoryDTO())).collect(Collectors.toList());
    }


}
