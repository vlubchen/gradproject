package com.github.vlubchen.gradproject.web.lunch;

import com.github.vlubchen.gradproject.model.Dish;
import com.github.vlubchen.gradproject.model.Lunch;
import com.github.vlubchen.gradproject.model.Restaurant;
import com.github.vlubchen.gradproject.repository.DishRepository;
import com.github.vlubchen.gradproject.repository.LunchRepository;
import com.github.vlubchen.gradproject.repository.RestaurantRepository;
import com.github.vlubchen.gradproject.to.LunchTo;
import com.github.vlubchen.gradproject.util.LunchUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
@CacheConfig(cacheNames = "lunches")
public class AdminLunchController extends AbstractLunchController {
    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/lunches";

    private final RestaurantRepository restaurantRepository;

    private final DishRepository dishRepository;

    public AdminLunchController(LunchRepository lunchRepository, RestaurantRepository restaurantRepository,
                                DishRepository dishRepository) {
        super(lunchRepository);
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
    }

    @GetMapping
    @Cacheable
    @Override
    public List<LunchTo> getOnToday(@PathVariable int restaurantId) {
        return super.getOnToday(restaurantId);
    }

    @GetMapping("/by-date")
    @Cacheable(key = "{#date, #restaurantId}")
    @Override
    public List<LunchTo> getByDate(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                          LocalDate date, @PathVariable int restaurantId) {
        return super.getByDate(date, restaurantId);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<LunchTo> get(@PathVariable int id, @PathVariable int restaurantId) {
        return super.get(id, restaurantId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    @Transactional
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete lunch item with id={}", id);
        lunchRepository.deleteExisted(id, restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    @Transactional
    public ResponseEntity<LunchTo> createWithLocation(@Valid @RequestBody LunchTo lunchTo,
                                                      @PathVariable int restaurantId) {
        log.info("create lunch item {}", lunchTo);
        checkNew(lunchTo);
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        Dish dish = dishRepository.getExistedByIdAndRestaurantId(lunchTo.getDishId(), restaurantId);
        Lunch lunch = LunchUtil.createNewFromTo(lunchTo, restaurant, dish);
        Lunch created = lunchRepository.save(lunch);
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
    @CacheEvict(allEntries = true)
    public void update(@Valid @RequestBody LunchTo lunchTo, @PathVariable int id,
                       @PathVariable int restaurantId) {
        log.info("update lunch item {} with id={}", lunchTo, id);
        assureIdConsistent(lunchTo, id);
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        Dish dish = dishRepository.getExistedByIdAndRestaurantId(lunchTo.getDishId(), restaurantId);
        Lunch lunch = LunchUtil.createNewFromTo(lunchTo, restaurant, dish);
        lunch.setId(lunchTo.getId());
        lunchRepository.save(lunch);
    }
}