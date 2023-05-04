package com.github.vlubchen.gradproject.web.restaurant;

import com.github.vlubchen.gradproject.model.Restaurant;
import com.github.vlubchen.gradproject.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = UserRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public final class UserRestaurantController extends AbstractRestaurantController {

    public static final String REST_URL = "/api/restaurants";

    public UserRestaurantController(RestaurantRepository restaurantRepository) {
        super(restaurantRepository);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        return super.get(id);
    }

    @GetMapping
    @Override
    public List<Restaurant> getAll() {
        return super.getAll();
    }
}