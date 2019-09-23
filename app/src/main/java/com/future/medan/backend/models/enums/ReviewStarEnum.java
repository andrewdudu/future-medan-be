package com.future.medan.backend.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReviewStarEnum {
    NONE,
    POOR,
    NEEDS_IMPROVEMENT,
    NICE,
    SUPERB,
    EXCELLENT
}
