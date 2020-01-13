package com.future.medan.backend.models.entity;

import com.future.medan.backend.constants.ReviewConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {

    @Column(name = ReviewConstant.REVIEW_STAR)
    private Integer rating;

    @Column(name = ReviewConstant.REVIEW_COMMENT)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ReviewConstant.PRODUCT_ID)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ReviewConstant.USER_ID)
    private User user;
}
