package com.github.vlubchen.gradproject.to;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DishTo extends NamedTo {

    @PositiveOrZero
    int price;

    public DishTo(Integer id, String name, int price) {
        super(id, name);
        this.price = price;
    }
}