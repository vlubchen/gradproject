package com.github.vlubchen.gradproject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Entity
@Table(name = "lunch_item", indexes = @Index(name = "lunch_unique_restaurant_date_dish_idx",
        columnList = "restaurant_id, created_date, dish_id", unique = true))

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class LunchItem extends BaseEntity {
    @Column(name = "created_date", nullable = false)
    @NotNull
    private LocalDate createdDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private Dish dish;

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    public LunchItem(Integer id, LocalDate createdDate, Restaurant restaurant, Dish dish, Integer price) {
        super(id);
        this.createdDate = createdDate;
        this.restaurant = restaurant;
        this.dish = dish;
        this.price = price;
    }

    public LunchItem(LunchItem lunchItem) {
        this(lunchItem.id, lunchItem.createdDate, lunchItem.restaurant, lunchItem.dish, lunchItem.price);
    }
}
