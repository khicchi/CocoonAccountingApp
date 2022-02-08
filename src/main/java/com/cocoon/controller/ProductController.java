package com.cocoon.controller;

import com.cocoon.dto.ProductDTO;
import com.cocoon.exception.CocoonException;
import com.cocoon.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public String getAllProducts(Model model){
        model.addAttribute("products", productService.getAllProducts());
        return "/product/product-list";
    }

    @GetMapping("/create")
    public String getCreateProductPage(Model model){
        model.addAttribute("product", new ProductDTO());
        //model.addAttribute("category", categoryRepository.getAll()); TODO @otto updated here after category repository created.
        return "/product/product-add";
    }

    @PostMapping("/create")
    public String saveProduct(ProductDTO productDTO){
        productService.save(productDTO);
        return "/product/product-list";
    }

    @GetMapping("/update/{id}")
    public String getUpdateProductPage(@PathVariable("id") Long id, Model model) throws CocoonException {
        model.addAttribute("product", productService.getProductById(id));
        //model.addAttribute("category", categoryRepository.getAll()); TODO @otto updated here after category repository created.
        return "/product/product-edit";
    }

    @PostMapping("/update")
    public String updateProduct(ProductDTO productDTO){
        productService.save(productDTO);
        return "/product/product-list";
    }


}
