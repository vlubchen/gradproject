package com.github.vlubchen.gradproject.web.restaurant;

import com.github.vlubchen.gradproject.model.Restaurant;
import com.github.vlubchen.gradproject.repository.RestaurantRepository;
import com.github.vlubchen.gradproject.to.RestaurantTo;
import com.github.vlubchen.gradproject.util.RestaurantUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.github.vlubchen.gradproject.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.vlubchen.gradproject.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminRestaurantController {

    public static final String REST_URL = "/api/admin/restaurants";

    private final RestaurantRepository restaurantRepository;

    public AdminRestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTo> get(@PathVariable int id) {
        log.info("get restaurant with id={}", id);
        return ResponseEntity.of(RestaurantUtil.getTo(restaurantRepository.findById(id)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant with id={}", id);
        restaurantRepository.deleteExisted(id);
    }

    @GetMapping
    public List<RestaurantTo> getAll() {
        log.info("get all restaurants");
        return RestaurantUtil.getRestaurantsTo(restaurantRepository.findAll());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = "restaurants", allEntries = true)
    public ResponseEntity<RestaurantTo> createWithLocation(@Valid @RequestBody RestaurantTo restaurantTo) {
        log.info("create restaurant {}", restaurantTo);
        checkNew(restaurantTo);
        Restaurant restaurant = RestaurantUtil.createNewFromTo(restaurantTo);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(RestaurantUtil.createTo(created));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    public void update(@Valid @RequestBody RestaurantTo restaurantTo, @PathVariable int id) {
        log.info("update restaurant {} with id={}", restaurantTo, id);
        assureIdConsistent(restaurantTo, id);
        Restaurant restaurant = restaurantRepository.getExisted(id);
        RestaurantUtil.updateFromTo(restaurant, restaurantTo);
        restaurantRepository.save(restaurant);
    }
}