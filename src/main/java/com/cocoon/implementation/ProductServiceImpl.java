package com.cocoon.implementation;

import com.cocoon.dto.ProductDTO;
import com.cocoon.entity.Product;
import com.cocoon.repository.ProductRepository;
import com.cocoon.service.ProductService;
import com.cocoon.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private MapperUtil mapperUtil;

    public ProductServiceImpl(ProductRepository productRepository, MapperUtil mapperUtil) {
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(product-> mapperUtil.convert(product, new ProductDTO())).collect(Collectors.toList());
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }
}
