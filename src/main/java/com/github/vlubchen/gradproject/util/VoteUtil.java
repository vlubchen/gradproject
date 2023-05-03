package com.github.vlubchen.gradproject.util;

import com.github.vlubchen.gradproject.model.Vote;
import com.github.vlubchen.gradproject.to.VoteTo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VoteUtil {
    public static List<VoteTo> getVotesTo(List<Vote> votes) {
        return votes.stream().map(VoteUtil::createTo).collect(Collectors.toList());
    }

    public static Optional<VoteTo> getTo(Optional<Vote> optionalVote) {
        return optionalVote.map(VoteUtil::createTo);
    }

    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getCreatedDate(), vote.getCreatedTime(),
                vote.getRestaurant().getId(), vote.getUser().getId());
    }
}