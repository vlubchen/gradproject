package com.github.vlubchen.gradproject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"created_date", "user_id", "restaurant_id"},
        name = "vote_unique_date_restaurant_user")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Vote extends NamedEntity {

    @Column(name = "created_date", nullable = false)
    @NotNull
    private LocalDate createdDate;

    @Column(name = "created_time", nullable = false)
    @NotNull
    private LocalTime createdTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private Restaurant restaurant;

    public Vote(Integer id, String name, LocalDate createdDate, LocalTime createdTime, User user, Restaurant restaurant) {
        super(id, name);
        this.createdDate = createdDate;
        this.createdTime = createdTime;
        this.user = user;
        this.restaurant = restaurant;
    }
}