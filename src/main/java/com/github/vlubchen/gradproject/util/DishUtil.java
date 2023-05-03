package com.github.vlubchen.gradproject.util;

import com.github.vlubchen.gradproject.model.Dish;
import com.github.vlubchen.gradproject.model.Restaurant;
import com.github.vlubchen.gradproject.to.DishTo;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class DishUtil {
    public static List<DishTo> getDishesTo(List<Dish> dishes) {
        return dishes.stream().map(DishUtil::createTo).collect(Collectors.toList());
    }

    public static Optional<DishTo> getTo(Optional<Dish> optionalDish) {
        return optionalDish.map(DishUtil::createTo);
    }

    public static Dish createNewFromTo(DishTo dishTo, Restaurant restaurant) {
        return new Dish(dishTo.getId(), dishTo.getName(), restaurant, dishTo.getPrice());
    }

    public static DishTo createTo(Dish dish) {
        return new DishTo(dish.getId(), dish.getName(), dish.getPrice());
    }

    public static void updateFromTo(Dish dish, DishTo dishTo) {
        dish.setName(dishTo.getName());
    }
}