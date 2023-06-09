package com.github.vlubchen.gradproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name = "dish", indexes = @Index(name = "dish_unique_restaurant_name_idx", columnList = "restaurant_id, name",
        unique = true))

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Dish extends NamedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @JsonBackReference
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Restaurant restaurant;

    @Column(name = "price", nullable = false)
    @Positive
    @JsonIgnore
    private Integer price;

    public Dish(Integer id, String name, Restaurant restaurant, Integer price) {
        super(id, name);
        this.restaurant = restaurant;
        this.price = price;
    }

    public Dish(Dish dish) {
        this(dish.id, dish.name, dish.restaurant, dish.price);
    }
}