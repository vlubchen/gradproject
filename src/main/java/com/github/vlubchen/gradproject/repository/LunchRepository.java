package com.github.vlubchen.gradproject.repository;

import com.github.vlubchen.gradproject.error.NotFoundException;
import com.github.vlubchen.gradproject.model.LunchItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface LunchRepository extends BaseRepository<LunchItem> {

    @EntityGraph(attributePaths = {"dish"})
    @Query("SELECT l FROM LunchItem l WHERE l.id =:id AND l.restaurant.id = :restaurantId")
    Optional<LunchItem> getByIdAndRestaurantId(int id, int restaurantId);

    @EntityGraph(attributePaths = {"dish"})
    @Query("""
            SELECT l FROM LunchItem l WHERE l.createdDate = :createdDate and l.restaurant.id = :restaurantId
            ORDER BY l.restaurant.id, l.dish.id
            """)
    List<LunchItem> getAllByDateAndRestaurantId(LocalDate createdDate, int restaurantId);

    @Transactional
    @Modifying
    @Query("DELETE FROM LunchItem l WHERE l.id =:id AND l.restaurant.id = :restaurantId")
    int delete(int id, int restaurantId);

    default void deleteExisted(int id, int restaurantId) {
        if (delete(id, restaurantId) == 0) {
            throw new NotFoundException("Lunch item with id=" + id +
                    " not found for restaurant with id=" + restaurantId);
        }
    }
}