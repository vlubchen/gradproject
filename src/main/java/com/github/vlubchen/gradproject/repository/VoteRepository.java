package com.github.vlubchen.gradproject.repository;

import com.github.vlubchen.gradproject.model.Vote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.createdDate = :createdDate AND v.user.id = :userId")
    Optional<Vote> getByDateAndUserId(LocalDate createdDate, int userId);

    @EntityGraph(attributePaths = {"restaurant"})
    @Query("SELECT v FROM Vote v WHERE v.id = :id")
    Optional<Vote> get(Integer id);

    @Query("SELECT v FROM Vote v WHERE v.createdDate = :createdDate")
    List<Vote> getAllByDate(LocalDate createdDate);
}