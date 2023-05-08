package com.github.vlubchen.gradproject.web.lunch;

import com.github.vlubchen.gradproject.model.Lunch;
import com.github.vlubchen.gradproject.repository.LunchRepository;
import com.github.vlubchen.gradproject.to.LunchTo;
import com.github.vlubchen.gradproject.util.JsonUtil;
import com.github.vlubchen.gradproject.util.LunchUtil;
import com.github.vlubchen.gradproject.web.AbstractControllerTest;
import com.github.vlubchen.gradproject.web.restaurant.AdminRestaurantController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static com.github.vlubchen.gradproject.web.lunch.LunchTestData.*;
import static com.github.vlubchen.gradproject.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static com.github.vlubchen.gradproject.web.user.UserTestData.ADMIN_MAIL;
import static com.github.vlubchen.gradproject.web.user.UserTestData.USER_MAIL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminLunchControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestaurantController.REST_URL + '/' + RESTAURANT1_ID + "/lunches";

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private LunchRepository lunchRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + LUNCH_ID_1))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(LUNCH_MATCHER.contentJson(LunchUtil.createTo(lunchItem1,
                        lunchItem1.getRestaurant(), lunchItem1.getDish())));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + LUNCH_ID_1))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(lunchRepository.getByIdAndRestaurantId(LUNCH_ID_1, RESTAURANT1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + LUNCH_ID_1))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + LUNCH_ID_1))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "by-date?date=" + DateTimeFormatter
                .ofPattern("yyyy-MM-dd").format(LocalDate.now())))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(LUNCH_MATCHER.contentJson(LunchUtil.getLunchesTo(lunchOnToday)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getTodayByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(LUNCH_MATCHER.contentJson(LunchUtil.getLunchesTo(lunchOnToday)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getByWrongDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "by-date?date=" + DateTimeFormatter
                .ofPattern("yyyy-MM-dd").format(LocalDate.of(2023, 3, 1))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(LUNCH_MATCHER.contentJson(Collections.emptyList()));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Lunch updated = getUpdated();
        updated.setId(LUNCH_ID_1);
        LunchTo updatedTo = LunchUtil.createTo(updated, updated.getRestaurant(), updated.getDish());
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + LUNCH_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        LUNCH_MATCHER.assertMatch(LunchUtil.getTo(lunchRepository.getByIdAndRestaurantId(LUNCH_ID_1, RESTAURANT1_ID))
                .orElseThrow(), LunchUtil.createTo(updated, updated.getRestaurant(), updated.getDish()));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Lunch newLunch = getNew();
        LunchTo newLunchTo = LunchUtil.createTo(newLunch, newLunch.getRestaurant(), newLunch.getDish());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newLunchTo)))
                .andExpect(status().isCreated());

        LunchTo created = LUNCH_MATCHER.readFromJson(action);
        int newId = created.id();
        newLunchTo.setId(newId);
        LUNCH_MATCHER.assertMatch(created, newLunchTo);
        LUNCH_MATCHER.assertMatch(LunchUtil.getTo(lunchRepository.getByIdAndRestaurantId(newId, RESTAURANT1_ID))
                .orElseThrow(), newLunchTo);
    }
}