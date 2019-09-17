package com.future.medan.backend.model.entity;

import com.future.medan.backend.model.constants.BaseEntityConstant;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
abstract class BaseEntity {

    @Id
    @Column(name = BaseEntityConstant.ID)
    @GeneratedValue(generator = BaseEntityConstant.SYSTEM_UUID)
    @GenericGenerator(name = BaseEntityConstant.SYSTEM_UUID, strategy = BaseEntityConstant.STRATEGY_UUID2)
    private String id;
}
