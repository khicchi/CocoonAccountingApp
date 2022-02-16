package com.cocoon.repository;

import com.cocoon.entity.InvoiceProduct;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface InvoiceProductRepo extends JpaRepository<InvoiceProduct, Long> {

    Set<InvoiceProduct> findAllByInvoiceId(Long id);
    List<InvoiceProduct> findAllByProductId(Long id);
    InvoiceProduct findInvoiceProductByInvoiceIdAndNameAndQtyAndPriceAndTax(Long id, String name, int qty, int price, int tax);
}
