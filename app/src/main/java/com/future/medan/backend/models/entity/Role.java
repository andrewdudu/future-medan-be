package com.future.medan.backend.models.entity;

import com.future.medan.backend.models.constants.RoleConstant;
import com.future.medan.backend.models.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Enumerated(EnumType.ORDINAL)
    @Column(name = RoleConstant.ROLE_NAME)
    private RoleEnum role_name;
}
