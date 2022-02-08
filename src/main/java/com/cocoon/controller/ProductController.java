package com.cocoon.controller;

import com.cocoon.dto.ProductDTO;
import com.cocoon.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String createProduct(Model model){
        model.addAttribute("product", new ProductDTO());
        //model.addAttribute("category", categoryRepository.getAll()) todo @otto updated here after category repository created.
        return "/product/product-add";
    }


}
