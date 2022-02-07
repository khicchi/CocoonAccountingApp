package com.cocoon.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Component
public class BaseEntityListener extends AuditingEntityListener {

    @PrePersist
    public void onPrePersist(BaseEntity baseEntity) {
        baseEntity.setCreatedBy("1");//todo @kicchi this will be changed with the logged in user id at security level
        baseEntity.setCreatedTime(LocalDateTime.now());
        onPreUpdate(baseEntity);
    }

    @PreUpdate
    public void onPreUpdate(BaseEntity baseEntity) {
        baseEntity.setUpdatedBy("1");//todo @kicchi this will be changed with the logged in user id at security level
        baseEntity.setUpdatedTime(LocalDateTime.now());
    }
}
