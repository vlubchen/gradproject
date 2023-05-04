package com.github.vlubchen.gradproject.repository;

import com.github.vlubchen.gradproject.error.IllegalRequestDataException;
import com.github.vlubchen.gradproject.error.NotFoundException;
import com.github.vlubchen.gradproject.model.Dish;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
    @Query("SELECT d FROM Dish d WHERE d.id = :id and d.restaurant.id = :restaurantId")
    Optional<Dish> getByIdAndRestaurantId(int id, int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id = :restaurantId ORDER BY d.name")
    List<Dish> getAllByRestaurantId(int restaurantId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id =:id AND d.restaurant.id = :restaurantId")
    int delete(int id, int restaurantId);

    default void deleteExisted(int id, int restaurantId) {
        if (delete(id, restaurantId) == 0) {
            throw new NotFoundException("Dish with id=" + id +
                    " not found for restaurant with id=" + restaurantId);
        }
    }

    default Dish getExistedByIdAndRestaurantId(int id, int restaurantId) {
        return getByIdAndRestaurantId(id, restaurantId)
                .orElseThrow(() -> new IllegalRequestDataException("Dish with id=" + id +
                        " not found for restaurant with id=" + restaurantId));
    }
}