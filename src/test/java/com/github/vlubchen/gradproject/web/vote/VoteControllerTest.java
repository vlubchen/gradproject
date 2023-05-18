package com.github.vlubchen.gradproject.web.vote;

import com.github.vlubchen.gradproject.model.Vote;
import com.github.vlubchen.gradproject.repository.VoteRepository;
import com.github.vlubchen.gradproject.to.VoteTo;
import com.github.vlubchen.gradproject.util.VoteUtil;
import com.github.vlubchen.gradproject.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.github.vlubchen.gradproject.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static com.github.vlubchen.gradproject.web.restaurant.RestaurantTestData.RESTAURANT3_ID;
import static com.github.vlubchen.gradproject.web.user.UserTestData.*;
import static com.github.vlubchen.gradproject.web.vote.VoteController.REST_URL;
import static com.github.vlubchen.gradproject.web.vote.VoteTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getOnTodayByUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "today"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VoteUtil.createTo(voteOfUser3)));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllByUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VoteUtil.getVotesTo(votesByUser)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "by-date?date=" + DateTimeFormatter
                .ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(2))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VoteUtil.createTo(voteOfUser1)));
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void createWithLocation() throws Exception {
        Vote newVote = VoteTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL_SLASH + "today")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(RESTAURANT1_ID)))
                .andExpect(status().isCreated());

        VoteTo created = VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VoteTo newVoteTo = VoteUtil.createTo(newVote);
        VOTE_MATCHER.assertMatch(created, newVoteTo);
        VOTE_MATCHER.assertMatch(VoteUtil.getTo(voteRepository.get(newId)).orElseThrow(), newVoteTo);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = USER_MAIL)
    void createDuplicate() throws Exception {
        Vote newVote = VoteTestData.getUpdated();
        newVote.setUser(user);
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + "today")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(RESTAURANT3_ID)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = USER_MAIL)
    void updateBeforeMaxTime() throws Exception {
        Vote updatedVote = VoteTestData.getUpdated();
        LocalTime initMaxTimeForUpdateVote = VoteController.maxTimeForUpdateVote;
        VoteController.setMaxTimeForUpdateVote(LocalTime.MAX);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + "today")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(RESTAURANT3_ID)))
                .andDo(print())
                .andExpect(status().isNoContent());

        VOTE_MATCHER.assertMatch(VoteUtil.getTo(voteRepository.get(USER_VOTE_ID_5))
                .orElseThrow(), VoteUtil.createTo(updatedVote));
        VoteController.setMaxTimeForUpdateVote(initMaxTimeForUpdateVote);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = USER_MAIL)
    void updateAfterMaxTime() throws Exception {
        LocalTime initMaxTimeForUpdateVote = VoteController.maxTimeForUpdateVote;
        VoteController.setMaxTimeForUpdateVote(LocalTime.MIN);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + "today")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(RESTAURANT3_ID)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        VoteController.setMaxTimeForUpdateVote(initMaxTimeForUpdateVote);
    }
}