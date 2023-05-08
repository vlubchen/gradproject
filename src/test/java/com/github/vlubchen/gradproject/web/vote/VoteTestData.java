package com.github.vlubchen.gradproject.web.vote;

import com.github.vlubchen.gradproject.model.Vote;
import com.github.vlubchen.gradproject.to.VoteTo;
import com.github.vlubchen.gradproject.web.MatcherFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.github.vlubchen.gradproject.web.restaurant.RestaurantTestData.*;
import static com.github.vlubchen.gradproject.web.user.UserTestData.*;

public class VoteTestData {

    public static final MatcherFactory.Matcher<VoteTo> VOTE_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class, "createdTime");

    public static final int USER_VOTE_ID = 5;
    public static final int ADMIN_VOTE_ID = 6;

    public static final int NOT_FOUND = 100;

    public static final Vote voteOfUser = new Vote(USER_VOTE_ID, LocalDate.now(), LocalTime.now(), user, restaurant2);
    public static final Vote voteOfAdmin = new Vote(ADMIN_VOTE_ID, LocalDate.now(), LocalTime.now(), admin, restaurant1);

    public static final List<Vote> votesOnToday = List.of(voteOfAdmin, voteOfUser);

    public static Vote getNew() {
        return new Vote(null, LocalDate.now(), LocalTime.now(), guest, restaurant1);
    }

    public static Vote getUpdated() {
        return new Vote(USER_VOTE_ID, LocalDate.now(), LocalTime.now(), user, restaurant3);
    }
}
