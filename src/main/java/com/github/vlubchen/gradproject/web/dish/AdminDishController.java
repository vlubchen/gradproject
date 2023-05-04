package com.github.vlubchen.gradproject.web.dish;

import com.github.vlubchen.gradproject.model.Dish;
import com.github.vlubchen.gradproject.repository.DishRepository;
import com.github.vlubchen.gradproject.repository.RestaurantRepository;
import com.github.vlubchen.gradproject.to.DishTo;
import com.github.vlubchen.gradproject.util.DishUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.vlubchen.gradproject.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.vlubchen.gradproject.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminDishController {
    private final RestaurantRepository restaurantRepository;

    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/dishes";

    private final DishRepository dishRepository;

    public AdminDishController(DishRepository dishRepository,
                               RestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishTo> get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get dish with id={} for restaurantId={}", id, restaurantId);
        return ResponseEntity.of(DishUtil.getTo(dishRepository.getByIdAndRestaurantId(id, restaurantId)));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete dish with id={} for restaurantId={}", id, restaurantId);
        dishRepository.getExistedByIdAndRestaurantId(id, restaurantId);
        dishRepository.deleteExisted(id, restaurantId);
    }

    @GetMapping
    public List<DishTo> getAllByRestaurantId(@PathVariable int restaurantId) {
        log.info("get dishes for restaurantId={}", restaurantId);
        return DishUtil.getDishesTo(dishRepository.getAllByRestaurantId(restaurantId));
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DishTo> createWithLocation(@Valid @RequestBody DishTo dishTo, @PathVariable int restaurantId) {
        log.info("create dish {} for restaurantId={}", dishTo, restaurantId);

        checkNew(dishTo);
        Dish dish = DishUtil.createNewFromTo(dishTo, restaurantRepository.getExisted(restaurantId));
        Dish created = dishRepository.save(dish);
        Map<String, Integer> urlParams = new HashMap<>();
        urlParams.put("restaurantId", restaurantId);
        urlParams.put("id", created.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(urlParams).toUri();
        return ResponseEntity.created(uriOfNewResource).body(DishUtil.createTo(created));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody DishTo dishTo, @PathVariable int id, @PathVariable int restaurantId) {
        log.info("update dish {} with id={} for restaurantId={}", dishTo, id, restaurantId);
        assureIdConsistent(dishTo, id);
        Dish dish = dishRepository.getExistedByIdAndRestaurantId(id, restaurantId);
        DishUtil.updateFromTo(dish, dishTo);
        dishRepository.save(dish);
    }
}