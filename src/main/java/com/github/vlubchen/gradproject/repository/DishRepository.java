package com.github.vlubchen.gradproject.repository;

import com.github.vlubchen.gradproject.model.Dish;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
}