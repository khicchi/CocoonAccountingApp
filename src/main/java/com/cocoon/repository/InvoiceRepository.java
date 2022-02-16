package com.cocoon.repository;

import com.cocoon.entity.Company;
import com.cocoon.entity.Invoice;
import com.cocoon.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findInvoicesByCompanyAndInvoiceType(Company company, InvoiceType invvoiceType);
}
