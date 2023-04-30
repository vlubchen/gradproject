package com.github.vlubchen.gradproject.repository;

import com.github.vlubchen.gradproject.model.Restaurant;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
}