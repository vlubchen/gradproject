package com.github.vlubchen.gradproject.web.dish;

import com.github.vlubchen.gradproject.model.Dish;
import com.github.vlubchen.gradproject.repository.DishRepository;
import com.github.vlubchen.gradproject.to.DishTo;
import com.github.vlubchen.gradproject.util.DishUtil;
import com.github.vlubchen.gradproject.util.JsonUtil;
import com.github.vlubchen.gradproject.web.AbstractControllerTest;
import com.github.vlubchen.gradproject.web.restaurant.AdminRestaurantController;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.github.vlubchen.gradproject.config.RestExceptionHandler.EXCEPTION_DUPLICATE_DISH_NAME;
import static com.github.vlubchen.gradproject.web.dish.DishTestData.*;
import static com.github.vlubchen.gradproject.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static com.github.vlubchen.gradproject.web.user.UserTestData.ADMIN_MAIL;
import static com.github.vlubchen.gradproject.web.user.UserTestData.USER_MAIL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminDishControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestaurantController.REST_URL + '/' + RESTAURANT1_ID + "/dishes";

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private DishRepository dishRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + DISH1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DishTestData.DISH_MATCHER.contentJson(DishUtil.createTo(DishTestData.dish1)));
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
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(DishUtil.getDishesTo(dishes)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + DISH2_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(dishRepository.getByIdAndRestaurantId(DISH2_ID, RESTAURANT1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Dish updated = DishTestData.getUpdated();
        updated.setId(DishTestData.DISH2_ID);
        DishTo updatedTo = DishUtil.createTo(updated);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + DISH2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(DishUtil.getTo(dishRepository.getByIdAndRestaurantId(DISH2_ID, RESTAURANT1_ID))
                .orElseThrow(), updatedTo);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Dish newDish = DishTestData.getNew();
        DishTo newDishTo = DishUtil.createTo(newDish);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDishTo)))
                .andExpect(status().isCreated());

        DishTo created = DISH_MATCHER.readFromJson(action);
        int newId = created.id();
        newDishTo.setId(newId);
        DISH_MATCHER.assertMatch(created, newDishTo);
        DISH_MATCHER.assertMatch(DishUtil.getTo(dishRepository.getByIdAndRestaurantId(newId, RESTAURANT1_ID))
                .orElseThrow(), newDishTo);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicateName() throws Exception {
        Dish invalid = new Dish(null, dish1.getName(), dish1.getRestaurant(), dish1.getPrice());
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().string(Matchers.containsString(EXCEPTION_DUPLICATE_DISH_NAME)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicateName() throws Exception {
        Dish duplicate = new Dish(dish1);
        duplicate.setName(DishTestData.dish2.getName());
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(duplicate)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().string(Matchers.containsString(EXCEPTION_DUPLICATE_DISH_NAME)));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }
}