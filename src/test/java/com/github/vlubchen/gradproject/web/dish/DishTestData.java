package com.github.vlubchen.gradproject.web.dish;

import com.github.vlubchen.gradproject.model.Dish;
import com.github.vlubchen.gradproject.to.DishTo;
import com.github.vlubchen.gradproject.web.MatcherFactory;

import java.util.List;

import static com.github.vlubchen.gradproject.web.restaurant.RestaurantTestData.*;

public class DishTestData {
    public static final MatcherFactory.Matcher<DishTo> DISH_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(DishTo.class);

    public static final int DISH1_ID = 1;
    public static final int DISH2_ID = 2;
    public static final int DISH3_ID = 3;
    public static final int DISH4_ID = 4;
    public static final int DISH5_ID = 5;

    public static final int NOT_FOUND = 100;

    public static final Dish dish1 = new Dish(DISH1_ID, "Грибной суп", restaurant1, 25000);
    public static final Dish dish2 = new Dish(DISH2_ID, "Стейк", restaurant1, 80000);
    public static final Dish dish3 = new Dish(DISH3_ID, "Винегрет", restaurant1, 15000);
    public static final Dish dish4 = new Dish(DISH4_ID, "Компот", restaurant1, 5000);
    public static final Dish dish5 = new Dish(DISH5_ID, "Новое блюдо", restaurant1, 40000);

    public static final List<Dish> dishes = List.of(dish1, dish2, dish3, dish4, dish5);

    public static Dish getNew() {
        return new Dish(null, "NewDish", restaurant2, 10000);
    }

    public static Dish getUpdated() {
        return new Dish(DISH5_ID, "UpdatedName", restaurant1, 10000);
    }
}