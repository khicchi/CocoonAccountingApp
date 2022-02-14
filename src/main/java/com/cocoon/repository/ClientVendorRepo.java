package com.cocoon.repository;

import com.cocoon.entity.ClientVendor;
import com.cocoon.exception.CocoonException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientVendorRepo extends JpaRepository<ClientVendor, Long> {

    boolean existsByCompanyName(String companyName) throws CocoonException;

    ClientVendor findByEmail(String email);

}
