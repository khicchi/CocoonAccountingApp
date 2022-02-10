package com.cocoon.repository;

import com.cocoon.entity.InvoiceNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceNumberRepo extends JpaRepository<InvoiceNumber, Long> {
}
