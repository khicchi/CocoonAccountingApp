package com.cocoon.repository;

import com.cocoon.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepo extends JpaRepository<TestEntity, Integer> {
    TestEntity getById(int id);
}
