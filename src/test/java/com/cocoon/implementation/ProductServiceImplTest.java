package com.cocoon.implementation;

import com.cocoon.dto.CompanyDTO;
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

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {ProductServiceImpl.class})
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
    public void getAllProducts() {

        when(productRepository.findAll()).thenReturn(productList);
        when(mapperUtil.convert(any(), (ProductDTO) any())).thenReturn(new ProductDTO());

        assertEquals(2, productServiceImpl.getAllProducts().size());

        verify(productRepository).findAll();

    }

    @Test
    void getAllProductsByCompany() {

        when(companyService.getCompanyByLoggedInUser()).thenReturn(companyDTO);
        when(productRepository.findAllByCompanyId(any())).thenReturn(productList);
        when(mapperUtil.convert(any(), (ProductDTO) any())).thenReturn(new ProductDTO());

        assertEquals(2, productServiceImpl.getAllProductsByCompany().size());

        verify(companyService).getCompanyByLoggedInUser();
        verify(productRepository).findAllByCompanyId(any());
    }

    @Test
    void save() {

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
    void getProductById() {

        when(productRepository.findById(product.getId())).thenReturn(Optional.ofNullable(product));
        when(mapperUtil.convert(any(), (ProductDTO) any())).thenReturn(new ProductDTO());
        when(productRepository.findById(0L)).thenThrow(new NoSuchProductException(0L));

        assertEquals(true, productRepository.findById(product.getId()).isPresent());
        assertNotNull(productServiceImpl.getProductById(product.getId()));
        assertThrows(NoSuchProductException.class, () -> productServiceImpl.getProductStatusById(0L));

    }

    @Test
    void testGetProductById() {

        when(productRepository.findById((Long) any())).thenReturn(Optional.of(product));
        when(mapperUtil.convert((Object) any(), (ProductDTO) any())).thenReturn(productDTO);

        assertSame(productDTO, this.productServiceImpl.getProductById(123L));

        verify(productRepository).findById((Long) any());
        verify(mapperUtil).convert((Object) any(), (ProductDTO) any());
    }

    /**
     * Method under test: {@link ProductServiceImpl#getProductById(Long)}
     */
    @Test
    void testGetProductById2() {
        Company company = new Company();
        company.setAddress1("42 Main St");
        company.setAddress2("42 Main St");
        company.setCategories(new ArrayList<>());
        company.setClient(new ArrayList<>());
        company.setCreatedBy(1L);
        company.setCreatedTime(LocalDateTime.of(1, 1, 1, 1, 1));
        company.setEmail("jane.doe@example.org");
        company.setEnabled((byte) 'A');
        company.setEstablishmentDate(LocalDate.ofEpochDay(1L));
        company.setId(123L);
        company.setIsDeleted(true);
        company.setPhone("4105551212");
        company.setRepresentative("Representative");
        company.setState(new State());
        company.setTitle("Dr");
        company.setUpdatedBy(1L);
        company.setUpdatedTime(LocalDateTime.of(1, 1, 1, 1, 1));
        company.setUser(new ArrayList<>());
        company.setZip("21654");

        Category category = new Category();
        category.setCompany(company);
        category.setCreatedBy(1L);
        category.setCreatedTime(LocalDateTime.of(1, 1, 1, 1, 1));
        category.setDescription("The characteristics of someone or something");
        category.setEnabled(true);
        category.setId(123L);
        category.setIsDeleted(true);
        category.setUpdatedBy(1L);
        category.setUpdatedTime(LocalDateTime.of(1, 1, 1, 1, 1));

        Company company1 = new Company();
        company1.setAddress1("42 Main St");
        company1.setAddress2("42 Main St");
        company1.setCategories(new ArrayList<>());
        company1.setClient(new ArrayList<>());
        company1.setCreatedBy(1L);
        company1.setCreatedTime(LocalDateTime.of(1, 1, 1, 1, 1));
        company1.setEmail("jane.doe@example.org");
        company1.setEnabled((byte) 'A');
        company1.setEstablishmentDate(LocalDate.ofEpochDay(1L));
        company1.setId(123L);
        company1.setIsDeleted(true);
        company1.setPhone("4105551212");
        company1.setRepresentative("Representative");
        company1.setState(new State());
        company1.setTitle("Dr");
        company1.setUpdatedBy(1L);
        company1.setUpdatedTime(LocalDateTime.of(1, 1, 1, 1, 1));
        company1.setUser(new ArrayList<>());
        company1.setZip("21654");

        Product product = new Product();
        product.setCategory(category);
        product.setCompany(company1);
        product.setCreatedBy(1L);
        product.setCreatedTime(LocalDateTime.of(1, 1, 1, 1, 1));
        product.setDescription("The characteristics of someone or something");
        product.setEnabled((byte) 'A');
        product.setId(123L);
        product.setIsDeleted(true);
        product.setLowLimitAlert(1);
        product.setName("Name");
        product.setProductStatus(ProductStatus.ACTIVE);
        product.setQty(1);
        product.setTax(1);
        product.setUnit(Unit.LIBRE);
        product.setUpdatedBy(1L);
        product.setUpdatedTime(LocalDateTime.of(1, 1, 1, 1, 1));
        Optional<Product> ofResult = Optional.of(product);
        when(this.productRepository.findById((Long) any())).thenReturn(ofResult);
        when(this.mapperUtil.convert((Object) any(), (ProductDTO) any()))
                .thenThrow(new NoSuchProductException("An error occurred"));
        assertThrows(NoSuchProductException.class, () -> this.productServiceImpl.getProductById(123L));
        verify(this.productRepository).findById((Long) any());
        verify(this.mapperUtil).convert((Object) any(), (ProductDTO) any());
    }

    @Test
    void update() {

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
    public void getProductStatusById() {

        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(productRepository.findById(0L)).thenThrow(new NoSuchProductException(0L));

        assertEquals(true, productRepository.findById(product.getId()).isPresent());
        assertEquals(ProductStatus.ACTIVE, productServiceImpl.getProductStatusById(product.getId()));
        assertNotEquals(ProductStatus.PASSIVE, productServiceImpl.getProductStatusById(product2.getId()));
        assertThrows(NoSuchProductException.class, () -> productServiceImpl.getProductStatusById(0L));

    }

    @Test
    void getUnitById() {

        when(productRepository.findById(product.getId())).thenReturn(Optional.ofNullable(product));
        when(productRepository.findById(0L)).thenThrow(new NoSuchProductException(0L));

        assertEquals(true, productRepository.findById(product.getId()).isPresent());
        assertEquals(Unit.PIECES, productServiceImpl.getUnitById(product.getId()));
        assertThrows(NoSuchProductException.class, () -> productServiceImpl.getProductStatusById(0L));

    }

    @Test
    void deleteById() {

        when(productRepository.findById(product.getId())).thenReturn(Optional.ofNullable(product));
        when(productRepository.save(product)).thenReturn(product);

        assertNotNull(productRepository.findById(product.getId()));
        assertNotNull(productRepository.save(product));

        verify(productRepository).findById(product.getId());
        verify(productRepository).save(product);

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