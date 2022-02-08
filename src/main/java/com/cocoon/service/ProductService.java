package com.cocoon.service;

import com.cocoon.dto.ProductDTO;
import com.cocoon.entity.Product;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getAllProducts();
    void save(Product product);


}
