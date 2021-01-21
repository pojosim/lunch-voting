package com.example.lunchvoting.web.controller.rest;

import com.example.lunchvoting.entity.Restaurant;
import com.example.lunchvoting.service.VoteService;
import com.example.lunchvoting.util.DtoUtil;
import com.example.lunchvoting.web.dto.ResultVote;
import com.example.lunchvoting.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.lunchvoting.TestData.*;
import static com.example.lunchvoting.TestUtil.userHttpBasic;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends AbstractControllerTest {
    public static final String REST_VOTE_URL = RestaurantController.REST_URL + "/%s/vote";

    @Test
    void getTodayRestaurantsWithMenu() throws Exception {
        perform(get(RestaurantController.REST_URL).with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(JsonUtil.writeValue(List.of(
                        RESTAURANT_1, RESTAURANT_2).stream()
                        .map(restaurant -> DtoUtil.createRestaurantDto(restaurant, mapRestaurantIdToCountVote.get(restaurant.getId())))
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
                .andExpect(content().json(JsonUtil.writeValue(allSavedRestaurants)));
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
    void voteForRestaurant() throws Exception {
        HashMap<Integer, Integer> mapRestaurantIdToCountVoteCopy = new HashMap<>(mapRestaurantIdToCountVote);
        mapRestaurantIdToCountVoteCopy.merge(RESTAURANT_1.getId(), 1, Integer::sum);

        List<ResultVote> actualVotes = List.of(RESTAURANT_1, RESTAURANT_2).stream()
                .map(restaurant -> new ResultVote(restaurant, mapRestaurantIdToCountVoteCopy.get(restaurant.getId())))
                .collect(Collectors.toList());

        perform(post(String.format(REST_VOTE_URL, RESTAURANT_1.getId())).with(userHttpBasic(USER4)))
                .andExpect(status().isOk());
    }

    @Test
    void voteForNotFoundRestaurant() throws Exception {
        perform(post(String.format(REST_VOTE_URL, 2000)).with(userHttpBasic(USER4)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void repeatVoteForRestaurant() throws Exception {
        if (LocalTime.now().isAfter(VoteService.END_TIME_IS_VOTING)) {
            perform(post(String.format(REST_VOTE_URL, RESTAURANT_3.getId())).with(userHttpBasic(USER5)))
                    .andExpect(status().isUnprocessableEntity());
        } else {
            perform(post(String.format(REST_VOTE_URL, RESTAURANT_2.getId())).with(userHttpBasic(USER5)))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void createRestaurant() throws Exception {
        Restaurant expectedRestaurant = createNewRestaurant(TODAY_DATE);
        perform(post(RestaurantController.REST_URL_ADMIN)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(expectedRestaurant))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().json(JsonUtil.writeValue(expectedRestaurant)));

        assertNotNull(restaurantRepository.findRestaurantsByName(expectedRestaurant.getName()));
    }

    @Test
    void createRestaurantForbidden() throws Exception {
        Restaurant expectedRestaurant = createNewRestaurant(TODAY_DATE);
        perform(post(RestaurantController.REST_URL_ADMIN)
                .with(userHttpBasic(USER1))
                .content(JsonUtil.writeValue(expectedRestaurant))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteRestaurant() throws Exception {
        perform(delete(RestaurantController.REST_URL_ADMIN + "/1000").with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(restaurantRepository.existsById(RESTAURANT_1.getId()));
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