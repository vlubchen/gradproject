package com.github.vlubchen.gradproject.web.lunch;

import com.github.vlubchen.gradproject.model.Lunch;
import com.github.vlubchen.gradproject.to.LunchTo;
import com.github.vlubchen.gradproject.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static com.github.vlubchen.gradproject.web.dish.DishTestData.*;
import static com.github.vlubchen.gradproject.web.restaurant.RestaurantTestData.restaurant1;

public class LunchTestData {

    public static final MatcherFactory.Matcher<LunchTo> LUNCH_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(LunchTo.class);

    public static final int LUNCH_ID_1 = 25;
    public static final int LUNCH_ID_2 = 26;

    public static final int NOT_FOUND = 100;

    public static final Lunch lunchItem1 = new Lunch(LUNCH_ID_1, LocalDate.now(), restaurant1, dish1, 10000);
    public static final Lunch lunchItem2 = new Lunch(LUNCH_ID_2, LocalDate.now(), restaurant1, dish2, 40000);
    public static final Lunch lunchItem3 = new Lunch(27, LocalDate.now(), restaurant1, dish3, 70000);
    public static final Lunch lunchItem4 = new Lunch(28, LocalDate.now(), restaurant1, dish4, 2000);

    public static final List<Lunch> lunchOnToday = List.of(lunchItem1, lunchItem2, lunchItem3, lunchItem4);

    public static Lunch getNew() {
        return new Lunch(null, LocalDate.now(), restaurant1, dish5, 20000);
    }

    public static Lunch getUpdated() {
        return new Lunch(LUNCH_ID_2, LocalDate.now(), restaurant1, dish5, 20000);
    }
}