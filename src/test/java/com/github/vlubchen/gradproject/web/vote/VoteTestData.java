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

    public static final int USER_VOTE_ID_1 = 1;
    public static final int USER_VOTE_ID_3 = 3;
    public static final int USER_VOTE_ID_5 = 5;

    public static final Vote voteOfUser1 = new Vote(USER_VOTE_ID_1, LocalDate.now().minusDays(2),
            LocalTime.now(), user, restaurant2);
    public static final Vote voteOfUser2 = new Vote(USER_VOTE_ID_3, LocalDate.now().minusDays(1),
            LocalTime.now(), user, restaurant1);
    public static final Vote voteOfUser3 = new Vote(USER_VOTE_ID_5, LocalDate.now(),
            LocalTime.now(), user, restaurant2);

    public static final List<Vote> votesByUser = List.of(voteOfUser1, voteOfUser2, voteOfUser3);

    public static Vote getNew() {
        return new Vote(null, LocalDate.now(), LocalTime.now(), guest, restaurant1);
    }

    public static Vote getUpdated() {
        return new Vote(USER_VOTE_ID_5, LocalDate.now(), LocalTime.now(), user, restaurant3);
    }
}
