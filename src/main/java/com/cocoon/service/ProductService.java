package com.cocoon.service;

import com.cocoon.dto.ProductDTO;
import com.cocoon.entity.Product;
import com.cocoon.exception.CocoonException;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getAllProducts();
    void save(ProductDTO productDTO);
    ProductDTO getProductById(Long id) throws CocoonException;
    void update(ProductDTO productDTO);

}
