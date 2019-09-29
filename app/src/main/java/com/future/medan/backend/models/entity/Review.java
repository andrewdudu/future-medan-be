package com.future.medan.backend.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.medan.backend.models.constants.ReviewConstant;
import com.future.medan.backend.models.enums.ReviewStarEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {

    @Enumerated(EnumType.ORDINAL)
    @Column(name = ReviewConstant.REVIEW_STAR)
    private ReviewStarEnum rating;

    @Column(name = ReviewConstant.REVIEW_COMMENT)
    private String comment;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ReviewConstant.PRODUCT_ID)
    private Product product;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ReviewConstant.USER_ID)
    private User user;
}
