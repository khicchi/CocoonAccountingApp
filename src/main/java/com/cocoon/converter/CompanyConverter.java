package com.cocoon.converter;

import com.cocoon.dto.CompanyDTO;
import com.cocoon.entity.Company;
import com.cocoon.repository.CompanyRepo;
import com.cocoon.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
@ConfigurationPropertiesBinding
public class CompanyConverter implements Converter<String, CompanyDTO> {

    private CompanyRepo companyRepo;
    MapperUtil mapperUtil;

    public CompanyConverter(@Lazy CompanyRepo companyRepo, MapperUtil mapperUtil) {
        this.companyRepo = companyRepo;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public CompanyDTO convert(String id) {
        Company company = companyRepo.findById(Long.parseLong(id)).orElseThrow();
        return mapperUtil.convert(company, new CompanyDTO());
    }
}
