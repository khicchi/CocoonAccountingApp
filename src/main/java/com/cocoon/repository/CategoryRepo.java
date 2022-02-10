package com.cocoon.repository;

import com.cocoon.entity.Category;
import com.cocoon.exception.CocoonException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {

    boolean existsByDescription(String  desc) throws CocoonException;
}