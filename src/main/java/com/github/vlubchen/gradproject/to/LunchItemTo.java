package com.github.vlubchen.gradproject.to;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LunchItemTo extends BaseTo {

    @NotNull
    LocalDate createdDate;

    int restaurantId;

    int dishId;

    @Range(min = 1000, max = 1000000)
    @NotNull
    int price;

    public LunchItemTo(Integer id, LocalDate createdDate, int restaurantId, int dishId, int price) {
        super(id);
        this.createdDate = createdDate;
        this.restaurantId = restaurantId;
        this.dishId = dishId;
        this.price = price;
    }
}