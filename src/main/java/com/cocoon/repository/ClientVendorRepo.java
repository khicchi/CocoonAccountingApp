package com.cocoon.repository;

import com.cocoon.entity.Client;
import com.cocoon.enums.CompanyType;
import com.cocoon.exception.CocoonException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientVendorRepo extends JpaRepository<Client, Long> {

    boolean existsByCompanyName(String companyName) throws CocoonException;

    boolean existsByCompanyNameAndCompanyId(String companyName, Long usersCompanyId) throws CocoonException;

    Optional<Client> findById(Long id);

    List<Client> findAllByType(CompanyType type);

}
