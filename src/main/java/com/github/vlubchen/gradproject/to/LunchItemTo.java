package com.github.vlubchen.gradproject.to;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LunchItemTo extends BaseTo {

    @NotNull
    LocalDate createdDate;

    int restaurantId;

    int dishId;

    @PositiveOrZero
    int price;

    public LunchItemTo(Integer id, LocalDate createdDate, int restaurantId, int dishId, int price) {
        super(id);
        this.createdDate = createdDate;
        this.restaurantId = restaurantId;
        this.dishId = dishId;
        this.price = price;
    }
}