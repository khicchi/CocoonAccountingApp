package com.cocoon.repository;

import com.cocoon.entity.ClientVendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientVendorRepo extends JpaRepository<ClientVendor, Long> {

}
