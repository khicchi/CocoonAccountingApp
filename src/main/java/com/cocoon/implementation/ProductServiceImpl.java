package com.cocoon.implementation;

import com.cocoon.dto.InvoiceDTO;
import com.cocoon.dto.ProductDTO;
import com.cocoon.entity.Invoice;
import com.cocoon.entity.Product;
import com.cocoon.exception.CocoonException;
import com.cocoon.repository.ProductRepository;
import com.cocoon.service.InvoiceService;
import com.cocoon.service.ProductService;
import com.cocoon.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private InvoiceService invoiceService;
    private MapperUtil mapperUtil;

    public ProductServiceImpl(ProductRepository productRepository, InvoiceService invoiceService, MapperUtil mapperUtil) {
        this.productRepository = productRepository;
        this.invoiceService = invoiceService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(product-> mapperUtil.convert(product, new ProductDTO())).collect(Collectors.toList());
    }

    @Override
    public void save(ProductDTO productDTO) {
        Product product = mapperUtil.convert(productDTO, new Product());
        productRepository.save(product);
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
    public void update(ProductDTO productDTO) {
        Product product = mapperUtil.convert(productDTO, new Product());
        productRepository.save(product);
    }

    @Override
    public List<ProductDTO> getProductsByInvoiceId(Long id) {
        List<Product> products = productRepository.findProductsByInvoiceId2(id);
        return products.stream().map(product -> mapperUtil.convert(product, new ProductDTO())).collect(Collectors.toList());
    }
}
