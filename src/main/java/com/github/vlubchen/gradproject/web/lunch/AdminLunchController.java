package com.github.vlubchen.gradproject.web.lunch;

import com.github.vlubchen.gradproject.model.Dish;
import com.github.vlubchen.gradproject.model.LunchItem;
import com.github.vlubchen.gradproject.model.Restaurant;
import com.github.vlubchen.gradproject.repository.DishRepository;
import com.github.vlubchen.gradproject.repository.LunchRepository;
import com.github.vlubchen.gradproject.repository.RestaurantRepository;
import com.github.vlubchen.gradproject.to.LunchItemTo;
import com.github.vlubchen.gradproject.util.LunchUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.vlubchen.gradproject.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.vlubchen.gradproject.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminLunchController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminLunchController {
    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/lunch-items";

    protected final LunchRepository lunchRepository;

    private final RestaurantRepository restaurantRepository;

    private final DishRepository dishRepository;

    public AdminLunchController(LunchRepository lunchRepository, RestaurantRepository restaurantRepository,
                                DishRepository dishRepository) {
        this.lunchRepository = lunchRepository;
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
    }

    @GetMapping("/by-date")
    public List<LunchItemTo> getByDate(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                       LocalDate date, @PathVariable int restaurantId) {
        log.info("get lunch items by date {} for restaurantId={}", date, restaurantId);
        return LunchUtil.getLunchItemsTo(lunchRepository.getAllByDateAndRestaurantId(date, restaurantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LunchItemTo> get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get lunch item with id={} for restaurantId={}", id, restaurantId);
        return ResponseEntity.of(LunchUtil.getTo(lunchRepository.getByIdAndRestaurantId(id, restaurantId)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete lunch item with id={}", id);
        lunchRepository.deleteExisted(id, restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    public ResponseEntity<LunchItemTo> createWithLocation(@Valid @RequestBody LunchItemTo lunchItemTo,
                                                          @PathVariable int restaurantId) {
        log.info("create lunch item {}", lunchItemTo);
        checkNew(lunchItemTo);
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        Dish dish = dishRepository.getExistedByIdAndRestaurantId(lunchItemTo.getDishId(), restaurantId);
        LunchItem lunchItem = LunchUtil.createNewFromTo(lunchItemTo, restaurant, dish);
        LunchItem created = lunchRepository.save(lunchItem);
        Map<String, Integer> urlParams = new HashMap<>();
        urlParams.put("restaurantId", restaurantId);
        urlParams.put("id", created.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(urlParams).toUri();
        return ResponseEntity.created(uriOfNewResource).body(LunchUtil.createTo(created, restaurant, dish));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    public void update(@Valid @RequestBody LunchItemTo lunchItemTo, @PathVariable int id,
                       @PathVariable int restaurantId) {
        log.info("update lunch item {} with id={}", lunchItemTo, id);
        assureIdConsistent(lunchItemTo, id);
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        Dish dish = dishRepository.getExistedByIdAndRestaurantId(lunchItemTo.getDishId(), restaurantId);
        LunchItem lunchItem = LunchUtil.createNewFromTo(lunchItemTo, restaurant, dish);
        lunchItem.setId(lunchItemTo.getId());
        lunchRepository.save(lunchItem);
    }
}