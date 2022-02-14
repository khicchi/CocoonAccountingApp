package com.cocoon.converter;

import com.cocoon.dto.CompanyDTO;
import com.cocoon.dto.ProductDTO;
import com.cocoon.entity.Company;
import com.cocoon.entity.Product;
import com.cocoon.repository.ProductRepository;
import com.cocoon.util.MapperUtil;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class ProductConverter implements Converter<String, ProductDTO> {

    ProductRepository productRepository;
    MapperUtil mapperUtil;

    public ProductConverter(@Lazy ProductRepository productRepository, MapperUtil mapperUtil) {
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public ProductDTO convert(String id) {
        Product product = productRepository.findById(Long.parseLong(id)).orElseThrow();
        return mapperUtil.convert(product, new ProductDTO());
    }
}
