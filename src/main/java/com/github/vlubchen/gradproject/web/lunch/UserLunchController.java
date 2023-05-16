package com.github.vlubchen.gradproject.web.lunch;

import com.github.vlubchen.gradproject.repository.LunchRepository;
import com.github.vlubchen.gradproject.to.LunchItemTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = UserLunchController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "lunchItems")
public class UserLunchController extends AbstractLunchController {
    static final String REST_URL = "/api/restaurants/{restaurantId}/lunch-items";

    public UserLunchController(LunchRepository lunchRepository) {
        super(lunchRepository);
    }

    @GetMapping("/by-date")
    @Cacheable(key = "{#date, #restaurantId}")
    @Override
    public List<LunchItemTo> getByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                       @PathVariable int restaurantId) {
        return super.getByDate(date, restaurantId);
    }

    @GetMapping
    @Cacheable(key = "{'today', #restaurantId}")
    @Override
    public List<LunchItemTo> getOnToday(@PathVariable int restaurantId) {
        return super.getOnToday(restaurantId);
    }
}