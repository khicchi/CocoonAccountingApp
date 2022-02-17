package com.cocoon.implementation;

import com.cocoon.dto.ClientVendorDTO;
import com.cocoon.entity.ClientVendor;
import com.cocoon.entity.Company;
import com.cocoon.enums.CompanyType;
import com.cocoon.exception.CocoonException;
import com.cocoon.repository.ClientVendorRepo;
import com.cocoon.repository.CompanyRepo;
import com.cocoon.service.ClientVendorService;
import com.cocoon.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientVendorServiceImpl implements ClientVendorService {

    private ClientVendorRepo clientVendorRepo;
    private MapperUtil mapperUtil;
    private CompanyRepo companyRepo;

    public ClientVendorServiceImpl(ClientVendorRepo clientVendorRepo, MapperUtil mapperUtil, CompanyRepo companyRepo) {
        this.clientVendorRepo = clientVendorRepo;
        this.mapperUtil = mapperUtil;
        this.companyRepo = companyRepo;
    }

    @Override
    public List<ClientVendorDTO> getAllClientsVendors() {
        List<ClientVendor> list = clientVendorRepo.findAll();
        return list.stream().map(client -> mapperUtil.convert(client, new ClientVendorDTO())).collect(Collectors.toList());
    }

    @Override
    public void save(ClientVendorDTO clientVendorDTO) throws CocoonException {
        //if same client name already exists in client_vendor table an exception is thrown
        if (clientVendorRepo.existsByCompanyName(clientVendorDTO.getCompanyName()))
            throw new CocoonException("This same name already saved to database.");

        //when a client is saved it is assumed that it's status is enabled
        clientVendorDTO.setEnabled(true);

        //if the length of the address exceeds 254 then it should be shortened
        if (clientVendorDTO.getAddress().length() > 254)
            throw new CocoonException("Address length should be lesser then 255");

        //region todo we need companyId. This section will be updated at security implementation @kicchi
        ClientVendor toSave = mapperUtil.convert(clientVendorDTO, new ClientVendor());
        Company company = new Company();
        company.setId(9L);
        toSave.setCompany(company);
        ///endregion

        ClientVendor savedClient = clientVendorRepo.save(toSave);
    }

    public List<ClientVendorDTO> getAllClientsVendorsActivesFirst() {
        List<ClientVendor> list = clientVendorRepo.findAll();
        list.sort((o1, o2) -> o2.getEnabled().compareTo(o1.getEnabled()));
        return list.stream().map(client -> mapperUtil.convert(client, new ClientVendorDTO())).collect(Collectors.toList());
    }

    @Override
    public ClientVendor findById(Long id) throws CocoonException {
        ClientVendor clientVendor = clientVendorRepo.findById(id).orElseThrow(()-> new CocoonException("Vendor/Client with " + id + " not exist"));
        return clientVendor;
    }

    @Override
    public ClientVendorDTO update(ClientVendorDTO clientVendorDTO) throws CocoonException {
        if (clientVendorDTO.getAddress().length() > 254)
            throw new CocoonException("Address length should be lesser then 255");
        ClientVendor updatedClientVendor = mapperUtil.convert(clientVendorDTO, new ClientVendor());
        //region todo we need companyId. This section will be updated at security implementation
        updatedClientVendor.setCompany(companyRepo.getById(9L));
        ClientVendor savedClientVendor = clientVendorRepo.save(updatedClientVendor);
        return mapperUtil.convert(savedClientVendor, new ClientVendorDTO());
    }

    @Override
    public void deleteClientVendor(Long id) throws CocoonException {
        ClientVendor clientVendor = clientVendorRepo.findById(id).orElseThrow(()-> new CocoonException("Vendor/Client with " + id + " not exist"));
        clientVendor.setIsDeleted(true);
        clientVendorRepo.save(clientVendor);
    }

    @Override
    public List<ClientVendorDTO> getAllClientVendorsByType(CompanyType type) {
        List<ClientVendor> clientVendors = clientVendorRepo.findAllByType(type);
        return clientVendors.stream().map(obj -> mapperUtil.convert(obj, new ClientVendorDTO())).collect(Collectors.toList());
    }
}
