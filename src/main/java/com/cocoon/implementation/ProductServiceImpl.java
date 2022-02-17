package com.cocoon.implementation;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.dto.ProductDTO;
import com.cocoon.entity.Category;
import com.cocoon.entity.Invoice;
import com.cocoon.entity.InvoiceProduct;
import com.cocoon.entity.Product;
import com.cocoon.enums.ProductStatus;
import com.cocoon.enums.Unit;
import com.cocoon.exception.CocoonException;
import com.cocoon.repository.CompanyRepo;
import com.cocoon.repository.InvoiceProductRepo;
import com.cocoon.repository.ProductRepository;
import com.cocoon.service.InvoiceService;
import com.cocoon.service.ProductService;
import com.cocoon.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private InvoiceService invoiceService;
    private MapperUtil mapperUtil;
    private CompanyRepo companyRepo;
    private InvoiceProductRepo invoiceProductRepo;


    public ProductServiceImpl(ProductRepository productRepository, InvoiceService invoiceService, MapperUtil mapperUtil, CompanyRepo companyRepo, InvoiceProductRepo invoiceProductRepo) {
        this.productRepository = productRepository;
        this.invoiceService = invoiceService;
        this.mapperUtil = mapperUtil;
        this.companyRepo = companyRepo;
        this.invoiceProductRepo = invoiceProductRepo;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(product-> mapperUtil.convert(product, new ProductDTO())).collect(Collectors.toList());
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        Product product = mapperUtil.convert(productDTO, new Product());
        product.setEnabled((byte) 1);
        product.setCompany(companyRepo.findById(9L).get()); // TODO implementation after security
        productRepository.save(product);
        return mapperUtil.convert(product, new ProductDTO());
    }


    @Override
    public ProductDTO getProductById(Long id) throws CocoonException {
        Optional<Product> product = productRepository.findById(id);
        if(!product.isPresent()){
            throw new CocoonException("There is no product belongs to this id " + id);
        }
        return mapperUtil.convert(product.get(), new ProductDTO());
    }

    @Override
    public void update(ProductDTO productDTO) throws CocoonException {
        Optional<Product> product = productRepository.findById(productDTO.getId());
        Product convertedProduct = mapperUtil.convert(productDTO, new Product());
        convertedProduct.setId(product.get().getId());
        convertedProduct.setEnabled(product.get().getEnabled());
        convertedProduct.setCompany(companyRepo.findById(9L).get()); // TODO implementation after security
        productRepository.save(convertedProduct);
    }

    @Override
    public Set<ProductDTO> getProductsByInvoiceId(Long id) {
        List<Product> products = productRepository.findProductsByInvoiceId2(id);
        return products.stream().map(product -> mapperUtil.convert(product, new ProductDTO())).collect(Collectors.toSet());
    }

    @Override
    public ProductStatus getProductStatusById(Long id) throws CocoonException {
        Optional<Product> product =productRepository.findById(id);
        if(!product.isPresent()){
            throw new CocoonException("There is no product belongs to this id " + id);
        }
        return product.get().getProductStatus();
    }

    @Override
    public Unit getUnitById(Long id) throws CocoonException {
        Optional<Product> product =productRepository.findById(id);
        if(!product.isPresent()){
            throw new CocoonException("There is no product belongs to this id " + id);
        }
        return product.get().getUnit();
    }

    @Override
    public void deleteById(Long id) throws CocoonException {
        Optional<Product> product = productRepository.findById(id);
        if(!product.isPresent()){
            throw new CocoonException("There is no product belongs to this id " + id);
        }
        // check if product has related invoice or not
        List<InvoiceProduct> invoiceProducts = invoiceProductRepo.findAllByProductId(id);
        if (invoiceProducts.size() ==0) {
            product.get().setIsDeleted(true); // soft delete
            productRepository.save(product.get());
        }
    }


    @Override
    public List<ProductDTO> findProductsByCategoryId(Long id) {
        List<Product> products = productRepository.findAllByCategoryId(id);
        List<ProductDTO> productDTOList = products.stream().map((p) -> mapperUtil.convert(p, new ProductDTO())).collect(Collectors.toList());
        return productDTOList;
    }
}
