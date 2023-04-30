package com.github.vlubchen.gradproject.repository;

import com.github.vlubchen.gradproject.model.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.createdDate = :createdDate AND v.user.id = :userId")
    Optional<Vote> findByDateAndUserId(LocalDate createdDate, int userId);

    @Query("""
            SELECT v FROM Vote v WHERE v.createdDate = :createdDate AND v.restaurant.id = :restaurantId
            ORDER BY v.createdTime DESC
            """)
    List<Vote> findAllByDateAndRestaurantId(LocalDate createdDate, int restaurantId);

    @Query("SELECT v FROM Vote v WHERE v.createdDate = :createdDate ORDER BY v.restaurant.id, v.user.id")
    List<Vote> findAllByDate(LocalDate createdDate);
}