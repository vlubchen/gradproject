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

    static final LocalTime MAX_TIME_FOR_UPDATE_VOTE = LocalTime.of(11, 0);

    static final String REST_URL = "/api/votes";

    private final VoteRepository voteRepository;

    private final RestaurantRepository restaurantRepository;

    public VoteController(VoteRepository voteRepository, RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/by-date")
    public List<VoteTo> getByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdDate) {
        log.info("get all votes by date");
        return getVotesTo(voteRepository.getAllByDate(createdDate));
    }

    @GetMapping
    public List<VoteTo> getOnToday() {
        log.info("get all votes on today");
        return getVotesTo(voteRepository.getAllByDate(LocalDate.now()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoteTo> get(@PathVariable int id) {
        log.info("get vote by id {}", id);
        return ResponseEntity.of(VoteUtil.getTo(voteRepository.get(id)));
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> getByUser(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        log.info("get current vote for user id={}", userId);
        return ResponseEntity.of(VoteUtil.getTo(voteRepository.getByDateAndUserId(LocalDate.now(), userId)));
    }

    @PostMapping(value = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> createWithLocation(@RequestBody Integer restaurantId,
                                                     @AuthenticationPrincipal AuthUser authUser) {
        log.info("create vote for user id={} with restaurant id={}", authUser.id(), restaurantId);
        Vote newVote = saveVote(restaurantId, authUser, true);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/profile")
                .buildAndExpand().toUri();
        return ResponseEntity.created(uriOfNewResource).body(VoteUtil.createTo(newVote));
    }

    @PutMapping(value = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
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
        if (!isNew && LocalTime.now().isAfter(MAX_TIME_FOR_UPDATE_VOTE)) {
            throw new IllegalRequestDataException(String.format("You can change your vote only until  %s",
                    MAX_TIME_FOR_UPDATE_VOTE));
        }
        newVote.setId(vote.getId());
        return voteRepository.save(newVote);
    }
}