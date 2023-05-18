package com.github.vlubchen.gradproject.web.vote;

import com.github.vlubchen.gradproject.error.IllegalRequestDataException;
import com.github.vlubchen.gradproject.model.Restaurant;
import com.github.vlubchen.gradproject.model.Vote;
import com.github.vlubchen.gradproject.repository.RestaurantRepository;
import com.github.vlubchen.gradproject.repository.VoteRepository;
import com.github.vlubchen.gradproject.to.VoteTo;
import com.github.vlubchen.gradproject.util.VoteUtil;
import com.github.vlubchen.gradproject.web.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.github.vlubchen.gradproject.util.VoteUtil.getVotesTo;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class VoteController {

    static LocalTime maxTimeForUpdateVote = LocalTime.of(11, 00);

    static final String REST_URL = "/api/votes";

    private final VoteRepository voteRepository;

    private final RestaurantRepository restaurantRepository;

    public VoteController(VoteRepository voteRepository, RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping(value = "/today", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> getByUser(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        log.info("get vote on today for user id={}", userId);
        return ResponseEntity.of(VoteUtil.getTo(voteRepository.getByDateAndUserId(LocalDate.now(), userId)));
    }

    @GetMapping("/by-date")
    public ResponseEntity<VoteTo> getByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                            @AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        log.info("get vote for user id={} by date={}", userId, date);
        return ResponseEntity.of(VoteUtil.getTo(voteRepository.getByDateAndUserId(date, userId)));
    }

    @GetMapping
    public List<VoteTo> getAllByUserId(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        log.info("get all votes for user id={}", userId);
        return getVotesTo(voteRepository.getAllByUserId(userId));
    }

    @PostMapping(value = "/today", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<VoteTo> createWithLocation(@RequestBody Integer restaurantId,
                                                     @AuthenticationPrincipal AuthUser authUser) {
        log.info("create vote for user id={} with restaurant id={}", authUser.id(), restaurantId);
        Vote newVote = saveVote(restaurantId, authUser, true);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/profile")
                .buildAndExpand().toUri();
        return ResponseEntity.created(uriOfNewResource).body(VoteUtil.createTo(newVote));
    }

    @PutMapping(value = "/today", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Integer restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update vote for user id {} with restaurant id={}", authUser.id(), restaurantId);
        saveVote(restaurantId, authUser, false);
    }

    private Vote saveVote(Integer restaurantId, AuthUser authUser, boolean isNew) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        Vote newVote = new Vote(null, currentDate, currentTime, authUser.getUser(), restaurant);
        Vote vote = voteRepository.getByDateAndUserId(currentDate, authUser.id()).orElse(newVote);
        if (vote.isNew() != isNew) {
            throw new IllegalRequestDataException("You should call " + (isNew ? "update" : "create") + " method");
        }
        if (!isNew && currentTime.isAfter(maxTimeForUpdateVote) && currentTime.isAfter(LocalTime.MIN)
                && currentTime.isBefore(LocalTime.MAX)) {
            throw new IllegalRequestDataException(String.format("You can change your vote only until  %s",
                    maxTimeForUpdateVote));
        }
        newVote.setId(vote.getId());
        return voteRepository.save(newVote);
    }

    public static void setMaxTimeForUpdateVote(LocalTime newMaxTimeForUpdateVote) {
        maxTimeForUpdateVote = newMaxTimeForUpdateVote;
    }
}