package com.cocoon.repository;

import com.cocoon.entity.InvoiceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceProductRepo extends JpaRepository<InvoiceProduct, Long> {
}
