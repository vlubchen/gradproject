package com.github.vlubchen.gradproject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "vote", indexes = @Index(name = "vote_unique_date_user_idx", columnList = "created_date, user_id",
        unique = true))

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Vote extends BaseEntity {

    @Column(name = "created_date", nullable = false)
    @NotNull
    private LocalDate createdDate;

    @Column(name = "created_time", nullable = false)
    @NotNull
    private LocalTime createdTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private Restaurant restaurant;

    public Vote(Integer id, LocalDate createdDate, LocalTime createdTime, User user, Restaurant restaurant) {
        super(id);
        this.createdDate = createdDate;
        this.createdTime = createdTime;
        this.user = user;
        this.restaurant = restaurant;
    }
}