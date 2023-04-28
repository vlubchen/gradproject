package com.github.vlubchen.gradproject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;


@Entity
@Table(name = "dish")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Dish extends NamedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Restaurant restaurant;

    @Column(name = "price", nullable = false)
    @NotNull
    @Positive
    private Integer price;


    public Dish(Integer id, String name, Integer price, Restaurant restaurant) {
        super(id, name);
        this.price = price;
        this.restaurant = restaurant;
    }
}