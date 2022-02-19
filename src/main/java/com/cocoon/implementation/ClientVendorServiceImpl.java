package com.cocoon.implementation;

import com.cocoon.dto.ClientDTO;
import com.cocoon.entity.Client;
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
    public List<ClientDTO> getAllClientsVendors() {
        List<Client> list = clientVendorRepo.findAll();
        return list.stream().map(client -> mapperUtil.convert(client, new ClientDTO())).collect(Collectors.toList());
    }

    @Override
    public void save(ClientDTO clientDTO) throws CocoonException {
        //if same client name already exists in client_vendor table an exception is thrown
        if (clientVendorRepo.existsByCompanyNameAndCompanyId(clientDTO.getCompanyName(), 9L))
            throw new CocoonException("A company with the same name exists.");

        //when a client is saved it is assumed that it's status is enabled
        clientDTO.setEnabled(true);

        //if the length of the address exceeds 254 then it should be shortened
        if (clientDTO.getAddress().length() > 254)
            throw new CocoonException("Address length should be lesser then 255");

        //region todo we need companyId. This section will be updated at security implementation @kicchi
        Client toSave = mapperUtil.convert(clientDTO, new Client());
        Company company = new Company();
        company.setId(9L);
        toSave.setCompany(company);
        ///endregion

        Client savedClient = clientVendorRepo.save(toSave);
    }

    public List<ClientDTO> getAllClientsVendorsActivesFirst() {
        List<Client> list = clientVendorRepo.findAll();
        list.sort((o1, o2) -> o2.getEnabled().compareTo(o1.getEnabled()));
        return list.stream().map(client -> mapperUtil.convert(client, new ClientDTO())).collect(Collectors.toList());
    }

    @Override
    public ClientDTO findById(Long id) throws CocoonException {
        Client client = clientVendorRepo.findById(id).orElseThrow(()-> new CocoonException("Vendor/Client with " + id + " not exist"));
        return mapperUtil.convert(client, new ClientDTO());
    }

    @Override
    public ClientDTO update(ClientDTO clientDTO) throws CocoonException {
        if (clientDTO.getAddress().length() > 254)
            throw new CocoonException("Address length should be lesser then 255");
        Client updatedClient = mapperUtil.convert(clientDTO, new Client());
        //region todo we need companyId. This section will be updated at security implementation
        updatedClient.setCompany(companyRepo.getById(9L));
        Client savedClient = clientVendorRepo.save(updatedClient);
        return mapperUtil.convert(savedClient, new ClientDTO());
    }

    @Override
    public void deleteClientVendor(Long id) throws CocoonException {
        Client client = clientVendorRepo.findById(id).orElseThrow(()-> new CocoonException("Vendor/Client with " + id + " not exist"));
        client.setIsDeleted(true);
        clientVendorRepo.save(client);
    }

    @Override
    public List<ClientDTO> getAllClientVendorsByType(CompanyType type) {
        List<Client> clients = clientVendorRepo.findAllByType(type);
        return clients.stream().map(obj -> mapperUtil.convert(obj, new ClientDTO())).collect(Collectors.toList());
    }
}
