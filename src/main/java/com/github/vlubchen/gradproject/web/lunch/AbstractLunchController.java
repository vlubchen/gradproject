package com.github.vlubchen.gradproject.web.lunch;

import com.github.vlubchen.gradproject.repository.LunchRepository;
import com.github.vlubchen.gradproject.to.LunchTo;
import com.github.vlubchen.gradproject.util.LunchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public class AbstractLunchController {
    protected final LunchRepository lunchRepository;

    protected AbstractLunchController(LunchRepository lunchRepository) {
        this.lunchRepository = lunchRepository;
    }

    public ResponseEntity<LunchTo> get(int id, int restaurantId) {
        log.info("get lunch with id={} for restaurantId={}", id, restaurantId);
        return ResponseEntity.of(LunchUtil.getTo(lunchRepository.getByIdAndRestaurantId(id, restaurantId)));
    }

    public List<LunchTo> getByDate(LocalDate date, int restaurantId) {
        log.info("get lunch by date {} for restaurantId={}", date, restaurantId);
        return LunchUtil.getLunchesTo(lunchRepository.getAllByDateAndRestaurantId(date, restaurantId));
    }

    public List<LunchTo> getOnToday(int restaurantId) {
        LocalDate date = LocalDate.now();
        log.info("get lunch on today for restaurantId={}", restaurantId);
        return LunchUtil.getLunchesTo(lunchRepository.getAllByDateAndRestaurantId(date, restaurantId));
    }
}