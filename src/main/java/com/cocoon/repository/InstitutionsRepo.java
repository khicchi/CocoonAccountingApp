package com.cocoon.repository;

import com.cocoon.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionsRepo extends JpaRepository<Institution, Long> {
}
