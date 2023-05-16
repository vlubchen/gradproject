package com.github.vlubchen.gradproject.util;

import com.github.vlubchen.gradproject.model.Dish;
import com.github.vlubchen.gradproject.model.LunchItem;
import com.github.vlubchen.gradproject.model.Restaurant;
import com.github.vlubchen.gradproject.to.LunchItemTo;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class LunchUtil {
    public static LunchItem createNewFromTo(LunchItemTo lunchItemTo, Restaurant restaurant, Dish dish) {
        return new LunchItem(null, lunchItemTo.getCreatedDate(), restaurant, dish, lunchItemTo.getPrice());
    }

    public LunchItemTo createTo(LunchItem lunchItem, Restaurant restaurant, Dish dish) {
        return new LunchItemTo(lunchItem.getId(), lunchItem.getCreatedDate(), restaurant.getId(),
                dish.getId(), lunchItem.getPrice());
    }

    public static List<LunchItemTo> getLunchItemsTo(List<LunchItem> lunchItems) {
        return lunchItems.stream()
                .map(lunch -> LunchUtil.createTo(lunch, lunch.getRestaurant(), lunch.getDish()))
                .collect(Collectors.toList());
    }

    public static Optional<LunchItemTo> getTo(Optional<LunchItem> optionalLunch) {
        return optionalLunch.map(lunch -> LunchUtil.createTo(lunch, lunch.getRestaurant(), lunch.getDish()));
    }
}