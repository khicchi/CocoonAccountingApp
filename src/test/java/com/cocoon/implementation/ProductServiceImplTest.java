package com.cocoon.implementation;

import com.cocoon.dto.ProductDTO;
import com.cocoon.entity.Category;
import com.cocoon.entity.Company;
import com.cocoon.entity.Product;
import com.cocoon.entity.State;
import com.cocoon.enums.ProductStatus;
import com.cocoon.enums.Unit;
import com.cocoon.exception.NoSuchProductException;
import com.cocoon.repository.InvoiceProductRepository;
import com.cocoon.repository.ProductRepository;
import com.cocoon.service.CompanyService;
import com.cocoon.util.MapperUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {ProductServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @MockBean
    private CompanyService companyService;


    @Mock
    ProductRepository productRepository;
    @Mock
    MapperUtil mapperUtil;
    @InjectMocks
    ProductServiceImpl productService;

    static Product product = new Product();
    static Product product2 = new Product();
    static ProductDTO productDTO = new ProductDTO();
    static List<Product> productList = new ArrayList<>();
    static Company company = new Company();

    @BeforeAll
    static void setUp() {
        product.setId(17L);
        product.setProductStatus(ProductStatus.ACTIVE);

        product.setId(18L);
        product.setProductStatus(ProductStatus.ACTIVE);

        productList.add(product);
        productList.add(product2);
    }

    @Test
    public void getAllProducts() {

        when(productRepository.findAll()).thenReturn(productList);
        when(mapperUtil.convert(any(), (ProductDTO) any())).thenReturn(new ProductDTO());

        //verify(productRepository).findAll();
        //verify(mapperUtil).convert(any(), (ProductDTO) any());
        assertEquals(2, productService.getAllProducts().size());

    }

    @Test
    public void getProductStatusById() {

        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(productRepository.findById(0L)).thenThrow(new NoSuchProductException());

        assertEquals(ProductStatus.ACTIVE, productService.getProductStatusById(product.getId()));
        assertNotEquals(ProductStatus.PASSIVE, productService.getProductStatusById(product2.getId()));
        assertThrows(NoSuchProductException.class, () -> productService.getProductStatusById(0L));

    }

    @Test
    void getAllProductsByCompany() {

    }
}