package com.github.vlubchen.gradproject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "lunch", uniqueConstraints = {@UniqueConstraint(columnNames = {"created_date", "restaurant_id", "dish_id"},
        name = "lunch_unique_date_restaurant_dish")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Lunch extends BaseEntity {
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
    @Positive(message = "Price must be positive")
    private Integer price;

    public Lunch(Integer id, LocalDate createdDate, Restaurant restaurant, Dish dish, Integer price) {
        super(id);
        this.createdDate = createdDate;
        this.restaurant = restaurant;
        this.dish = dish;
        this.price = price;
    }
}
