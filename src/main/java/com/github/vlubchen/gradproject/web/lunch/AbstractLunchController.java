package com.github.vlubchen.gradproject.web.lunch;

import com.github.vlubchen.gradproject.repository.LunchRepository;
import com.github.vlubchen.gradproject.to.LunchItemTo;
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

    public ResponseEntity<LunchItemTo> get(int id, int restaurantId) {
        log.info("get lunch item with id={} for restaurantId={}", id, restaurantId);
        return ResponseEntity.of(LunchUtil.getTo(lunchRepository.getByIdAndRestaurantId(id, restaurantId)));
    }

    public List<LunchItemTo> getByDate(LocalDate date, int restaurantId) {
        log.info("get lunch items by date {} for restaurantId={}", date, restaurantId);
        return LunchUtil.getLunchItemsTo(lunchRepository.getAllByDateAndRestaurantId(date, restaurantId));
    }

    public List<LunchItemTo> getOnToday(int restaurantId) {
        LocalDate date = LocalDate.now();
        log.info("get lunch items on today for restaurantId={}", restaurantId);
        return LunchUtil.getLunchItemsTo(lunchRepository.getAllByDateAndRestaurantId(date, restaurantId));
    }
}