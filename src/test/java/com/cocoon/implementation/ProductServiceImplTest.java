package com.cocoon.implementation;

import com.cocoon.dto.CompanyDTO;
import com.cocoon.dto.ProductDTO;
import com.cocoon.entity.Product;
import com.cocoon.enums.ProductStatus;
import com.cocoon.enums.Unit;
import com.cocoon.exception.NoSuchProductException;
import com.cocoon.repository.ProductRepository;
import com.cocoon.service.CompanyService;
import com.cocoon.util.MapperUtil;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private CompanyService companyService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MapperUtil mapperUtil;
    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    static Product product = new Product();
    static Product product2 = new Product();
    static List<Product> productList = new ArrayList<>();
    static ProductDTO productDTO = new ProductDTO();
    static CompanyDTO companyDTO = new CompanyDTO();

    @BeforeAll
    static void setUp() {
        product.setId(17L);
        product.setProductStatus(ProductStatus.ACTIVE);
        product.setUnit(Unit.PIECES);
        product.setIsDeleted(false);

        product2.setId(18L);
        product2.setProductStatus(ProductStatus.ACTIVE);

        productList.add(product);
        productList.add(product2);

        companyDTO.setId(1L);
        companyDTO.setTitle("Cyber");

    }

    @Test
    void testGetAllProducts() {

        when(productRepository.findAll()).thenReturn(productList);
        when(mapperUtil.convert(any(), (ProductDTO) any())).thenReturn(new ProductDTO());

        assertEquals(2, productServiceImpl.getAllProducts().size());

        verify(productRepository).findAll();

    }

    @Test
    void testGetAllProductsByCompany() {

        when(companyService.getCompanyByLoggedInUser()).thenReturn(companyDTO);
        when(productRepository.findAllByCompanyId(any())).thenReturn(productList);
        when(mapperUtil.convert(any(), (ProductDTO) any())).thenReturn(new ProductDTO());

        assertEquals(2, productServiceImpl.getAllProductsByCompany().size());

        verify(companyService).getCompanyByLoggedInUser();
        verify(productRepository).findAllByCompanyId(any());
    }

    @Test
    void testSave() {

        when(mapperUtil.convert(any(), (Product) any())).thenReturn(new Product());
        when(companyService.getCompanyByLoggedInUser()).thenReturn(companyDTO);
        when(productRepository.save(product)).thenReturn(product);

        assertNotNull(mapperUtil.convert(any(), any()));
        assertNotNull(companyService.getCompanyByLoggedInUser());
        assertNotNull(productRepository.save(product));

        verify(companyService).getCompanyByLoggedInUser();
        verify(productRepository).save(product);

    }

    @Test
    void testGetProductById() {

        when(productRepository.findById((Long) any())).thenReturn(Optional.of(product));
        when(mapperUtil.convert((Object) any(), (ProductDTO) any())).thenReturn(productDTO);

        assertSame(productDTO, this.productServiceImpl.getProductById(123L));

        verify(productRepository).findById((Long) any());
        verify(mapperUtil).convert((Object) any(), (ProductDTO) any());

    }

    @Test
    void testGetProductByIdWithException() {

        when(productRepository.findById((Long) any())).thenReturn(Optional.of(product));
        when(mapperUtil.convert((Object) any(), (ProductDTO) any()))
                .thenThrow(new NoSuchProductException("There is no product belongs to id " + any()));
        assertThrows(NoSuchProductException.class, () -> productServiceImpl.getProductById(123L));
        verify(productRepository).findById((Long) any());
        verify(mapperUtil).convert((Object) any(), (ProductDTO) any());
        
    }

    @Test
    void testUpdate() {

        when(mapperUtil.convert(any(), (Product) any())).thenReturn(new Product());
        when(companyService.getCompanyByLoggedInUser()).thenReturn(companyDTO);
        when(productRepository.save(product)).thenReturn(product);

        assertNotNull(mapperUtil.convert(any(), any()));
        assertNotNull(companyService.getCompanyByLoggedInUser());
        assertNotNull(productRepository.save(product));

        verify(companyService).getCompanyByLoggedInUser();
        verify(productRepository).save(product);

    }

    @Test
    void testGetProductStatusById() {

        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(productRepository.findById(0L)).thenThrow(new NoSuchProductException(0L));

        assertEquals(true, productRepository.findById(product.getId()).isPresent());
        assertEquals(ProductStatus.ACTIVE, productServiceImpl.getProductStatusById(product.getId()));
        assertNotEquals(ProductStatus.PASSIVE, productServiceImpl.getProductStatusById(product2.getId()));
        assertThrows(NoSuchProductException.class, () -> productServiceImpl.getProductStatusById(0L));

    }

    @Test
    void testGetUnitById() {

        when(productRepository.findById(product.getId())).thenReturn(Optional.ofNullable(product));
        when(productRepository.findById(0L)).thenThrow(new NoSuchProductException(0L));

        assertEquals(true, productRepository.findById(product.getId()).isPresent());
        assertEquals(Unit.PIECES, productServiceImpl.getUnitById(product.getId()));
        assertThrows(NoSuchProductException.class, () -> productServiceImpl.getProductStatusById(0L));

    }

    @Test
    void testDeleteById() {

        when(productRepository.findById(product.getId())).thenReturn(Optional.ofNullable(product));
        when(productRepository.save(product)).thenReturn(product);

        assertNotNull(productRepository.findById(product.getId()));
        assertNotNull(productRepository.save(product));

        verify(productRepository).findById(product.getId());
        verify(productRepository).save(product);

    }

    @Test
    void testFindProductsByCategoryId() {

        productServiceImpl.findProductsByCategoryId(1L);

        verify(productRepository).findAllByCategoryId(any());

    }

    @Test
    void testUpdateProductQuantity() {
    }

    @Test
    void testValidateProductQuantity() {
    }
}