package com.cocoon.service;

import com.cocoon.dto.CompanyDTO;
import com.cocoon.entity.Company;
import com.cocoon.exception.CocoonException;

import java.util.List;

public interface CompanyService {

    CompanyDTO getCompanyById(Long id) throws CocoonException;

    CompanyDTO update(CompanyDTO companyDTO)throws CocoonException;

    void close(CompanyDTO companyDTO)throws CocoonException;

    void open(CompanyDTO companyDTO)throws CocoonException;

    List<CompanyDTO> getAllCompanies();

    void save(CompanyDTO company) throws CocoonException;

}
