package com.github.vlubchen.gradproject.util;

import com.github.vlubchen.gradproject.model.Restaurant;
import com.github.vlubchen.gradproject.to.RestaurantTo;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class RestaurantUtil {

    public static Optional<RestaurantTo> getTo(Optional<Restaurant> optionalRestaurant) {
        return optionalRestaurant.map(RestaurantUtil::createTo);
    }

    public static List<RestaurantTo> getRestaurantsTo(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantUtil::createTo).collect(Collectors.toList());
    }

    public static RestaurantTo createTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getPhone(),
                restaurant.getAddress(), restaurant.getEmail());
    }

    public static Restaurant createNewFromTo(RestaurantTo restaurantTo) {
        return new Restaurant(null, restaurantTo.getName(), restaurantTo.getPhone(), restaurantTo.getAddress(),
                restaurantTo.getEmail().toLowerCase());
    }

    public static Restaurant updateFromTo(Restaurant restaurant, RestaurantTo restaurantTo) {
        restaurant.setName(restaurantTo.getName());
        restaurant.setPhone(restaurantTo.getPhone());
        restaurant.setAddress(restaurantTo.getAddress());
        restaurant.setEmail(restaurantTo.getEmail().toLowerCase());
        return restaurant;
    }
}
