package com.github.vlubchen.gradproject.repository;

import com.github.vlubchen.gradproject.model.Lunch;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface LunchRepository extends BaseRepository<Lunch> {

    @Query("SELECT l FROM Lunch l WHERE l.createdDate = :createdDate ORDER BY l.restaurant.id, l.dish.id")
    List<Lunch> findAllByDate(LocalDate createdDate);
}