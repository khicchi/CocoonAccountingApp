package com.cocoon.repository;

import com.cocoon.entity.Invoice;
import com.cocoon.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM product p INNER JOIN invoice_product_rel ipr on p.id = ipr.product_id INNER JOIN invoice i on ipr.invoice_id = i.id WHERE invoice_id =?1")
    List<Product> findProductsByInvoiceId2(Long id);

    // TODO - relation tablosundan çek...
   /*
    @Query(nativeQuery = true, value = "SELECT * FROM product p INNER JOIN category c on p.category_id = c.id WHERE c.id =?1")
    List<Product> getProductsByCategoryId2(Long id);
*/

    List<Product> findAllByCategoryId(Long id);


}
