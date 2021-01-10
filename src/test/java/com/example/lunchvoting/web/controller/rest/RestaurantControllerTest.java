package com.example.lunchvoting.web.controller.rest;

import com.example.lunchvoting.web.dto.RestaurantTo;
import com.example.lunchvoting.web.dto.ResultVote;
import com.example.lunchvoting.web.json.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.lunchvoting.TestData.*;
import static com.example.lunchvoting.TestUtil.userHttpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends AbstractControllerTest {
    private final String rest_vote_url = RestaurantController.REST_URL + "/%s/vote";

    @Test
    void getTodayRestaurantsWithMenu() throws Exception {
        perform(get(RestaurantController.REST_URL).with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(JsonUtil.writeValue(List.of(
                        RESTAURANT_1, RESTAURANT_2).stream()
                        .map(restaurant -> new RestaurantTo(restaurant, mapRestaurantIdToCountVote.get(restaurant.getId())))
                        .collect(Collectors.toList())
                )));
    }

    @Test
    void getTodayRestaurantsWithMenuUnauthorized() throws Exception {
        perform(get(RestaurantController.REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllRestaurants() throws Exception {
        perform(get(RestaurantController.REST_URL_ADMIN).with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(JsonUtil.writeValue(List.of(RESTAURANT_1, RESTAURANT_2, RESTAURANT_3))));
    }

    @Test
    void getAllRestaurantsUnauthorized() throws Exception {
        perform(get(RestaurantController.REST_URL_ADMIN))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllRestaurantsForbidden() throws Exception {
        perform(get(RestaurantController.REST_URL_ADMIN).with(userHttpBasic(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getVoteResults() throws Exception {
        List<ResultVote> actualVotes = List.of(RESTAURANT_1, RESTAURANT_2).stream()
                .map(restaurant -> new ResultVote(restaurant, mapRestaurantIdToCountVote.get(restaurant.getId())))
                .collect(Collectors.toList());

        perform(get(RestaurantController.REST_URL + "/vote-result").with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.writeValue(actualVotes)));
    }

    @Test
    void takeVoteForRestaurant() throws Exception {
        HashMap<Integer, Integer> mapRestaurantIdToCountVoteCopy = new HashMap<>(mapRestaurantIdToCountVote);
        mapRestaurantIdToCountVoteCopy.merge(RESTAURANT_1.getId(), 1, Integer::sum);

        List<ResultVote> actualVotes = List.of(RESTAURANT_1, RESTAURANT_2).stream()
                .map(restaurant -> new ResultVote(restaurant, mapRestaurantIdToCountVoteCopy.get(restaurant.getId())))
                .collect(Collectors.toList());

        perform(post(String.format(rest_vote_url, RESTAURANT_1.getId())).with(userHttpBasic(USER4)))
                .andExpect(status().isCreated())
                .andExpect(content().json(JsonUtil.writeValue(actualVotes)));
    }

    @Test
    void takeVoteForNotFoundRestaurant() throws Exception {
        perform(post(String.format(rest_vote_url, 2000)).with(userHttpBasic(USER4)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void takeRepeatVoteForRestaurant() throws Exception {
        perform(post(String.format(rest_vote_url, RESTAURANT_1.getId())).with(userHttpBasic(USER1)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createRestaurant() throws Exception {
        perform(post(RestaurantController.REST_URL_ADMIN)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(RESTAURANT_4_NON_SAVE))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().json(JsonUtil.writeValue(RESTAURANT_4_NON_SAVE)));

        Assertions.assertNotNull(restaurantRepository.findRestaurantsByName(RESTAURANT_4_NON_SAVE.getName()));
    }

    @Test
    void createRestaurantForbidden() throws Exception {
        perform(post(RestaurantController.REST_URL_ADMIN)
                .with(userHttpBasic(USER1))
                .content(JsonUtil.writeValue(RESTAURANT_4_NON_SAVE))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteRestaurant() throws Exception {
        perform(delete(RestaurantController.REST_URL_ADMIN + "/1000").with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());

        Assertions.assertEquals(restaurantRepository.findAll(), List.of(RESTAURANT_2, RESTAURANT_3));
    }

    @Test
    void deleteRestaurantForbidden() throws Exception {
        perform(delete(RestaurantController.REST_URL_ADMIN + "/1000").with(userHttpBasic(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteRestaurantNotFound() throws Exception {
        perform(delete(RestaurantController.REST_URL_ADMIN + "/10001").with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity());
    }
}