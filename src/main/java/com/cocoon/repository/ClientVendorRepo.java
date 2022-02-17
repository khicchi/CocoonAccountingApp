package com.cocoon.repository;

import com.cocoon.entity.ClientVendor;
import com.cocoon.enums.CompanyType;
import com.cocoon.exception.CocoonException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientVendorRepo extends JpaRepository<ClientVendor, Long> {

    boolean existsByCompanyName(String companyName) throws CocoonException;

    Optional<ClientVendor> findById(Long id);

    List<ClientVendor> findAllByType(CompanyType type);

}
