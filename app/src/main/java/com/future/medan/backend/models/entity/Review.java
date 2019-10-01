package com.future.medan.backend.models.entity;

import com.future.medan.backend.models.constants.ReviewConstant;
import com.future.medan.backend.models.enums.ReviewStarEnum;
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

    @Enumerated(EnumType.STRING)
    @Column(name = ReviewConstant.REVIEW_STAR)
    private ReviewStarEnum rating;

    @Column(name = ReviewConstant.REVIEW_COMMENT)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ReviewConstant.PRODUCT_ID)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ReviewConstant.USER_ID)
    private User user;
}
