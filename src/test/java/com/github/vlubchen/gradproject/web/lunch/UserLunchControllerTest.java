package com.github.vlubchen.gradproject.web.lunch;

import com.github.vlubchen.gradproject.util.LunchUtil;
import com.github.vlubchen.gradproject.web.AbstractControllerTest;
import com.github.vlubchen.gradproject.web.restaurant.UserRestaurantController;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static com.github.vlubchen.gradproject.web.lunch.LunchTestData.*;
import static com.github.vlubchen.gradproject.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static com.github.vlubchen.gradproject.web.user.UserTestData.USER_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserLunchControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserRestaurantController.REST_URL + '/' + RESTAURANT1_ID + "/lunches";

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + LUNCH_ID_1))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "by-date?date=" + DateTimeFormatter
                .ofPattern("yyyy-MM-dd").format(LocalDate.now())))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(LUNCH_MATCHER.contentJson(LunchUtil.getLunchesTo(lunchOnToday)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getTodayByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(LUNCH_MATCHER.contentJson(LunchUtil.getLunchesTo(lunchOnToday)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getByWrongDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "by-date?date=" + DateTimeFormatter
                .ofPattern("yyyy-MM-dd").format(LocalDate.of(2023, 3, 1))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(LUNCH_MATCHER.contentJson(Collections.emptyList()));
    }
}