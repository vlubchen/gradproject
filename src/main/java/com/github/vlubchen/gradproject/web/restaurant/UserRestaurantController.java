package com.github.vlubchen.gradproject.web.restaurant;

import com.github.vlubchen.gradproject.model.Restaurant;
import com.github.vlubchen.gradproject.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = UserRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "restaurants")
public class UserRestaurantController {

    public static final String REST_URL = "/api/restaurants";

    private final RestaurantRepository restaurantRepository;

    public UserRestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/{id}")
    @Cacheable
    public ResponseEntity<Restaurant> getByIdWithLunchItemsOnToday(@PathVariable int id) {
        log.info("get restaurant with id={} with lunch items on today", id);
        return ResponseEntity.of(restaurantRepository.getWithLunchItemsByDate(id, LocalDate.now()));
    }

    @GetMapping
    @Cacheable
    public List<Restaurant> getAllWithLunchItemsOnToday() {
        log.info("get all restaurants with lunch items on today");
        return restaurantRepository.getAllWithLunchItemsByDate(LocalDate.now());
    }
}