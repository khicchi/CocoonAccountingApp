package com.cocoon.implementation;

import com.cocoon.dto.ProductDTO;
import com.cocoon.entity.Product;
import com.cocoon.repository.ProductRepository;
import com.cocoon.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return null;
    }

    @Override
    public void save(Product product) {

    }
}
