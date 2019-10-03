package com.future.medan.backend.models.entity;

import com.future.medan.backend.constants.BaseEntityConstant;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
abstract class BaseEntity {

    @Id
    @Column(name = BaseEntityConstant.ID)
    @GeneratedValue(generator = BaseEntityConstant.SYSTEM_UUID)
    @GenericGenerator(name = BaseEntityConstant.SYSTEM_UUID, strategy = BaseEntityConstant.STRATEGY_UUID2)
    private String id;

    @CreationTimestamp
    private Date created_at;

    @UpdateTimestamp
    private Date updated_at;
}
