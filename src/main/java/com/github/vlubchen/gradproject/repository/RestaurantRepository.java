package com.github.vlubchen.gradproject.repository;

import com.github.vlubchen.gradproject.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @Query("SELECT DISTINCT r from Restaurant r JOIN FETCH r.lunchItems l WHERE l.createdDate=:date")
    List<Restaurant> getAllWithLunchItemsByDate(LocalDate date);

    @Query("SELECT r from Restaurant r JOIN FETCH r.lunchItems l WHERE r.id=:id AND l.createdDate=:date")
    Optional<Restaurant> getWithLunchItemsByDate(int id, LocalDate date);
}