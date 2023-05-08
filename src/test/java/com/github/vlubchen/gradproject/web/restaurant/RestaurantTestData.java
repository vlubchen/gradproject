package com.github.vlubchen.gradproject.web.restaurant;

import com.github.vlubchen.gradproject.model.Restaurant;
import com.github.vlubchen.gradproject.web.MatcherFactory;

import java.util.List;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class);

    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;
    public static final int RESTAURANT3_ID = 3;

    public static final int NOT_FOUND = 100;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "Sholly",
            "+7(8512)51-60-57", "ул. Урицкого, д.3", "upravl_sholi@am-house.ru");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "Izba",
            "+7(8512)51-81-91", "ул. Красная Набережная, д.8", "izba2012@inbox.ru");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT3_ID, "BeerHouse",
            "+7(8512)54-72-72", "ул. Савушкина, д.38", "pivdom@am-house.ru");

    public static final List<Restaurant> restaurants = List.of(restaurant3, restaurant2, restaurant1);

    public static Restaurant getNew() {
        return new Restaurant(null, "NewName", "00-00-00", "newAddress",
                "new_email@mail.ru");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, "UpdateName", "11-11-11", "UpdateAddress",
                "update_email@mail.ru");
    }
}