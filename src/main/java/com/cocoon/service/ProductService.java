package com.cocoon.service;

import com.cocoon.dto.ProductDTO;
import com.cocoon.entity.Invoice;
import com.cocoon.entity.Product;
import com.cocoon.enums.ProductStatus;
import com.cocoon.enums.Unit;
import com.cocoon.exception.CocoonException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ProductService {

    List<ProductDTO> getAllProducts();
    void save(ProductDTO productDTO);
    ProductDTO getProductById(Long id) throws CocoonException;
<<<<<<<<< Temporary merge branch 1
    void update(ProductDTO productDTO);
    Set<ProductDTO> getProductsByInvoiceId(Long id);
=========
    void update(ProductDTO productDTO) throws CocoonException;
    List<ProductDTO> getProductsByInvoiceId(Long id);
>>>>>>>>> Temporary merge branch 2
    ProductStatus getProductStatusById(Long id) throws CocoonException;
    Unit getUnitById(Long id) throws CocoonException;
}
