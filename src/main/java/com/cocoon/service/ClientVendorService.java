package com.cocoon.service;

import com.cocoon.dto.ClientVendorDTO;
import com.cocoon.entity.ClientVendor;
import com.cocoon.enums.CompanyType;
import com.cocoon.exception.CocoonException;

import java.util.List;

public interface ClientVendorService {

    List<ClientVendorDTO> getAllClientsVendors();

    void save(ClientVendorDTO clientVendorDTO) throws CocoonException;

    List<ClientVendorDTO> getAllClientsVendorsActivesFirst();

    ClientVendor findById(Long id) throws CocoonException;

    ClientVendorDTO update (ClientVendorDTO clientVendorDTO) throws CocoonException;

    void deleteClientVendor(Long id) throws CocoonException;

    List<ClientVendorDTO> getAllClientVendorsByType(CompanyType type);

}
