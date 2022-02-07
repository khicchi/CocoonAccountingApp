package com.cocoon.implementation;

import com.cocoon.dto.ProductDTO;
import com.cocoon.entity.Product;
import com.cocoon.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public List<ProductDTO> getAllProducts() {
        return null;
    }

    @Override
    public void save(Product product) {

    }
}
