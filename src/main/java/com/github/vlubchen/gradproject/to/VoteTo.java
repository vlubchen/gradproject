package com.github.vlubchen.gradproject.to;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VoteTo extends BaseTo {

    @NotNull
    LocalDate createdDate;

    @NotNull
    LocalTime createdTime;

    int restaurantId;

    int userId;

    public VoteTo(Integer id, LocalDate createdDate, LocalTime createdTime, int restaurantId, int userId) {
        super(id);
        this.createdDate = createdDate;
        this.createdTime = createdTime;
        this.restaurantId = restaurantId;
        this.userId = userId;
    }
}