package com.cocoon.implementation;

import com.cocoon.dto.CompanyDTO;
import com.cocoon.dto.ProductDTO;
import com.cocoon.entity.Category;
import com.cocoon.entity.Company;
import com.cocoon.entity.Product;
import com.cocoon.entity.State;
import com.cocoon.enums.InvoiceType;
import com.cocoon.enums.ProductStatus;
import com.cocoon.enums.Unit;
import com.cocoon.exception.NoSuchProductException;
import com.cocoon.repository.InvoiceProductRepository;
import com.cocoon.repository.ProductRepository;
import com.cocoon.service.CompanyService;
import com.cocoon.service.ProductService;
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
import java.util.stream.Collectors;

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

    @Mock
    private CompanyService companyService;
    @Mock
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private InvoiceProductRepository invoiceProductRepository;
    @Mock
    private MapperUtil mapperUtil;
    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    static Product product = new Product();
    static Product product2 = new Product();
    static ProductDTO productDTO = new ProductDTO();
    static ProductDTO productDTO2 = new ProductDTO();
    static List<Product> productList = new ArrayList<>();
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
    public void getAllProducts() {

        when(productRepository.findAll()).thenReturn(productList);
        when(mapperUtil.convert(any(), (ProductDTO) any())).thenReturn(new ProductDTO());

        assertEquals(2, productServiceImpl.getAllProducts().size());

    }

    @Test
    void getAllProductsByCompany() {

        when(companyService.getCompanyByLoggedInUser()).thenReturn(companyDTO);
        when(productRepository.findAllByCompanyId(any())).thenReturn(productList);
        when(mapperUtil.convert(any(), (ProductDTO) any())).thenReturn(new ProductDTO());

        assertEquals(2, productServiceImpl.getAllProductsByCompany().size());
    }

    @Test
    void save() {

        when(mapperUtil.convert(any(), (Product) any())).thenReturn(new Product());
        when(companyService.getCompanyByLoggedInUser()).thenReturn(companyDTO);
        when(productRepository.save(product)).thenReturn(product);

        assertNotNull(mapperUtil.convert(any(), any()));
        assertNotNull(companyService.getCompanyByLoggedInUser());
        assertNotNull(productRepository.save(product));

    }

    @Test
    void getProductById() {

        when(productRepository.findById(product.getId())).thenReturn(Optional.ofNullable(product));
        when(mapperUtil.convert(any(), (ProductDTO) any())).thenReturn(new ProductDTO());


        assertEquals(true, productRepository.findById(product.getId()).isPresent());
        assertNotNull(productServiceImpl.getProductById(product.getId()));

    }

    @Test
    void update() {
    }

    @Test
    public void getProductStatusById() {

        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(productRepository.findById(0L)).thenThrow(new NoSuchProductException());

        assertEquals(true, productRepository.findById(product.getId()).isPresent());
        assertEquals(ProductStatus.ACTIVE, productServiceImpl.getProductStatusById(product.getId()));
        assertNotEquals(ProductStatus.PASSIVE, productServiceImpl.getProductStatusById(product2.getId()));
        assertThrows(NoSuchProductException.class, () -> productServiceImpl.getProductStatusById(0L));

    }

    @Test
    void getUnitById() {

        when(productRepository.findById(product.getId())).thenReturn(Optional.ofNullable(product));

        assertEquals(true, productRepository.findById(product.getId()).isPresent());

        assertEquals(Unit.PIECES, productServiceImpl.getUnitById(product.getId()));
    }

    @Test
    void deleteById() {

      /*  when(productRepository.findById(product.getId())).thenReturn(Optional.ofNullable(product));
        when(productRepository.save(product)).thenReturn(product);

        productServiceImpl.deleteById(any());

        verify(productRepository).deleteById(any());

        productServiceImpl.deleteById(product.getId());

        assertEquals(true, productRepository.findById(product.getId()).isPresent());
        assertEquals(true, product.getIsDeleted());*/
    }

    @Test
    void findProductsByCategoryId() {

        productServiceImpl.findProductsByCategoryId(1L);

        verify(productRepository).findAllByCategoryId(any());


    }

    @Test
    void updateProductQuantity() {
    }

    @Test
    void validateProductQuantity() {
    }
}