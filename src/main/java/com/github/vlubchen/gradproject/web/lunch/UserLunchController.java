package com.github.vlubchen.gradproject.web.lunch;

import com.github.vlubchen.gradproject.repository.LunchRepository;
import com.github.vlubchen.gradproject.to.LunchTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = UserLunchController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UserLunchController extends AbstractLunchController {
    static final String REST_URL = "/api/restaurants/{restaurantId}/lunches";

    public UserLunchController(LunchRepository lunchRepository) {
        super(lunchRepository);
    }

    @GetMapping("/by-date")
    @Override
    public List<LunchTo> getByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                   @PathVariable int restaurantId) {
        return super.getByDate(date, restaurantId);
    }

    @GetMapping
    @Override
    public List<LunchTo> getOnToday(@PathVariable int restaurantId) {
        return super.getOnToday(restaurantId);
    }
}