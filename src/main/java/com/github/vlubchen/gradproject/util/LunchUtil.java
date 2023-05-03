package com.github.vlubchen.gradproject.util;

import com.github.vlubchen.gradproject.model.Dish;
import com.github.vlubchen.gradproject.model.Lunch;
import com.github.vlubchen.gradproject.model.Restaurant;
import com.github.vlubchen.gradproject.to.LunchTo;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class LunchUtil {
    public static Lunch createNewFromTo(LunchTo lunchTo, Restaurant restaurant, Dish dish) {
        return new Lunch(null, lunchTo.getCreatedDate(), restaurant, dish, lunchTo.getPrice());
    }

    public LunchTo createTo(Lunch lunch, Restaurant restaurant, Dish dish) {
        return new LunchTo(lunch.getId(), lunch.getCreatedDate(), restaurant.getId(),
                dish.getId(), lunch.getPrice());
    }

    public static List<LunchTo> getLunchesTo(List<Lunch> lunches) {
        return lunches.stream()
                .map(lunch -> LunchUtil.createTo(lunch, lunch.getRestaurant(), lunch.getDish()))
                .collect(Collectors.toList());
    }

    public static Optional<LunchTo> getTo(Optional<Lunch> optionalLunch) {
        return optionalLunch.map(lunch -> LunchUtil.createTo(lunch, lunch.getRestaurant(), lunch.getDish()));
    }
}