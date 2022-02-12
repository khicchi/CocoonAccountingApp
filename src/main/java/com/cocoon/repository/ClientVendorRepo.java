package com.cocoon.repository;

import com.cocoon.entity.ClientVendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientVendorRepo extends JpaRepository<ClientVendor, Long> {

    ClientVendor findByEmail(String email);

}
