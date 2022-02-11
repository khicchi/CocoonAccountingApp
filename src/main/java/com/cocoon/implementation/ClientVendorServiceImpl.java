package com.cocoon.implementation;

import com.cocoon.dto.ClientVendorDTO;
import com.cocoon.entity.ClientVendor;
import com.cocoon.repository.ClientVendorRepo;
import com.cocoon.service.ClientVendorService;
import com.cocoon.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientVendorServiceImpl implements ClientVendorService {

    private ClientVendorRepo clientVendorRepo;
    private MapperUtil mapperUtil;

    public ClientVendorServiceImpl(ClientVendorRepo clientVendorRepo, MapperUtil mapperUtil) {
        this.clientVendorRepo = clientVendorRepo;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<ClientVendorDTO> getAllClientsVendors() {
        List<ClientVendor> list = clientVendorRepo.findAll();
        return list.stream().map(client -> mapperUtil.convert(client, new ClientVendorDTO())).collect(Collectors.toList());
    }

    @Override
    public List<ClientVendorDTO> getAllClientsVendorsActivesFirst() {
        List<ClientVendor> list = clientVendorRepo.findAll();
        list.sort((o1, o2) -> o2.getEnabled().compareTo(o1.getEnabled()));
        return list.stream().map(client -> mapperUtil.convert(client, new ClientVendorDTO())).collect(Collectors.toList());
    }
}
