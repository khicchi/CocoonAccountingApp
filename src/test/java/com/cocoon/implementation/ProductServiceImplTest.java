package com.cocoon.implementation;

import com.cocoon.entity.Product;
import com.cocoon.enums.ProductStatus;
import com.cocoon.repository.ProductRepository;
import com.cocoon.util.MapperUtil;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;
    @Mock
    MapperUtil mapperUtil;
    @InjectMocks
    ProductServiceImpl productService;

    @Test
    public void getAllProducts() {
        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        product.setId(17L);
        product.setProductStatus(ProductStatus.ACTIVE);
        productList.add(product);

        Product product1 = new Product();
        productList.add(product1);

        Mockito.when(productRepository.findAll()).thenReturn(productList);

        assertEquals(2, productService.getAllProducts().size());

    }

    @Test
    public void getProductStatusById() {
        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        product.setId(17L);
        product.setProductStatus(ProductStatus.ACTIVE);
        productList.add(product);

        Product product1 = new Product();
        productList.add(product1);

        Mockito.when(productRepository.findById(product.getId())).thenReturn(java.util.Optional.of(product));

        assertEquals(ProductStatus.ACTIVE, productService.getProductStatusById(17L));

    }
}