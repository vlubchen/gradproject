package com.github.vlubchen.gradproject.web.lunch;

import com.github.vlubchen.gradproject.model.LunchItem;
import com.github.vlubchen.gradproject.to.LunchItemTo;
import com.github.vlubchen.gradproject.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static com.github.vlubchen.gradproject.web.dish.DishTestData.*;
import static com.github.vlubchen.gradproject.web.restaurant.RestaurantTestData.restaurant1;

public class LunchTestData {

    public static final MatcherFactory.Matcher<LunchItemTo> LUNCH_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(LunchItemTo.class);

    public static final int LUNCH_ITEM_ID_1 = 25;
    public static final int LUNCH_ITEM_ID_2 = 26;

    public static final int NOT_FOUND = 100;

    public static final LunchItem lunchItem1 = new LunchItem(LUNCH_ITEM_ID_1, LocalDate.now(), restaurant1, dish1, 10000);
    public static final LunchItem lunchItem2 = new LunchItem(LUNCH_ITEM_ID_2, LocalDate.now(), restaurant1, dish2, 40000);
    public static final LunchItem lunchItem3 = new LunchItem(27, LocalDate.now(), restaurant1, dish3, 70000);
    public static final LunchItem lunchItem4 = new LunchItem(28, LocalDate.now(), restaurant1, dish4, 2000);

    public static final List<LunchItem> lunchOnToday = List.of(lunchItem1, lunchItem2, lunchItem3, lunchItem4);

    public static LunchItem getNew() {
        return new LunchItem(null, LocalDate.now(), restaurant1, dish5, 20000);
    }

    public static LunchItem getUpdated() {
        return new LunchItem(LUNCH_ITEM_ID_2, LocalDate.now(), restaurant1, dish5, 20000);
    }
}