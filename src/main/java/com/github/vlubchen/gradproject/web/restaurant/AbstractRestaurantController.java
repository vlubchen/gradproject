package com.github.vlubchen.gradproject.web.restaurant;

import com.github.vlubchen.gradproject.model.Restaurant;
import com.github.vlubchen.gradproject.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
public abstract class AbstractRestaurantController {

    protected final RestaurantRepository restaurantRepository;

    protected AbstractRestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get restaurant with id={}", id);
        return ResponseEntity.of(restaurantRepository.findById(id));
    }

    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return restaurantRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }
}