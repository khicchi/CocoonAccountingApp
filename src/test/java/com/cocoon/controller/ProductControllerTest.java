package com.cocoon.controller;

import com.cocoon.dto.ProductDTO;
import com.cocoon.enums.ProductStatus;
import com.cocoon.enums.Unit;
import com.cocoon.service.CategoryService;
import com.cocoon.service.ProductService;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ProductController productController;

    @MockBean
    private ProductService productService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testGetAllProducts() throws Exception {
        when(productService.getAllProductsByCompany()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/product/list");
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("products"))
                .andExpect(MockMvcResultMatchers.view().name("product/product-list"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("product/product-list"));
    }

    @Test
    void getCreateProductPage() throws Exception {
        when(categoryService.getCategoryByCompany_Id()).thenReturn(new ArrayList<>());

        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(MockMvcRequestBuilders
                        .get("/product/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(MockMvcResultMatchers.model().attributeExists("product"))
                .andExpect(MockMvcResultMatchers.view().name("product/product-add"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("product/product-add"));
    }

    @Test
    void saveProduct() throws Exception {
        when(productService.save(any(ProductDTO.class))).thenReturn(new ProductDTO());

        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(MockMvcRequestBuilders
                        .post("/product/create"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/product/list"));
    }

    @Test
    void getUpdateProductPage() throws Exception {

        when(productService.getProductById((Long) any())).thenReturn(new ProductDTO());
        when(productService.getProductStatusById((Long) any())).thenReturn(ProductStatus.ACTIVE);
        when(productService.getUnitById((Long) any())).thenReturn(Unit.PIECES);
        when(categoryService.getCategoryByCompany_Id()).thenReturn(new ArrayList<>());

        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(MockMvcRequestBuilders
                        .get("/product/update/{id}", 17L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(6))
                .andExpect(MockMvcResultMatchers.model()
                        .attributeExists("categories", "product", "productStatus", "productStatuses", "unit", "units"))
                .andExpect(MockMvcResultMatchers.view().name("product/product-edit"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("product/product-edit"));

    }

    @Test
    void updateProduct() throws Exception {

        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(MockMvcRequestBuilders
                        .post("/product/update/{id}", 17L))
               .andExpect(MockMvcResultMatchers.view().name("redirect:/product/list"));

    }

    @Test
    void deleteProduct() throws Exception {

        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(MockMvcRequestBuilders
                        .post("/product/delete/{id}", 17L))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/product/list"));
    }
}