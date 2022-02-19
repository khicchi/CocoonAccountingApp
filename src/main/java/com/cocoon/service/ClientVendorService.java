package com.cocoon.service;

import com.cocoon.dto.ClientDTO;
import com.cocoon.enums.CompanyType;
import com.cocoon.exception.CocoonException;

import java.util.List;

public interface ClientVendorService {

    List<ClientDTO> getAllClientsVendors();

    void save(ClientDTO clientDTO) throws CocoonException;

    List<ClientDTO> getAllClientsVendorsActivesFirst();

    ClientDTO findById(Long id) throws CocoonException;

    ClientDTO update (ClientDTO clientDTO) throws CocoonException;

    void deleteClientVendor(Long id) throws CocoonException;

    List<ClientDTO> getAllClientVendorsByType(CompanyType type);

}
