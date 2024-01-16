package com.learning.dscatalog.projections;

import java.time.Instant;

public interface ProductProjection {
    Long geId();
    String  getName();
    String getDescription();
    Double getPrice();
    String getImgUrl();
    Instant getDate();
    Long getCategoryId();
    String getCategoryName();
}
