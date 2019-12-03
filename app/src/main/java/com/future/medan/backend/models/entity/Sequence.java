package com.future.medan.backend.models.entity;

import com.future.medan.backend.constants.SequenceConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sequences")
public class Sequence {

    @Id
    @Column(name = SequenceConstant.SEQUENCE_KEY)
    private String key;

    @Column(name = SequenceConstant.SEQUENCE_VALUE)
    private Integer value;
}
