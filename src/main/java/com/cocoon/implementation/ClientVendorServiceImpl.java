package com.cocoon.implementation;

import com.cocoon.repository.ClientVendorRepo;
import com.cocoon.service.ClientVendorService;
import com.cocoon.util.MapperUtil;
import org.springframework.stereotype.Service;

@Service
public class ClientVendorServiceImpl extends ClientVendorService {

    ClientVendorRepo clientVendorRepo;
    MapperUtil mapperUtil;

    public ClientVendorServiceImpl(ClientVendorRepo clientVendorRepo, MapperUtil mapperUtil) {
        this.clientVendorRepo = clientVendorRepo;
        this.mapperUtil = mapperUtil;
    }
}
