package com.example.lunchvoting.service;

import com.example.lunchvoting.AbstactTest;
import com.example.lunchvoting.entity.Restaurant;
import com.example.lunchvoting.entity.Vote;
import com.example.lunchvoting.repository.VoteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.lunchvoting.TestData.*;

class RestaurantServiceTest extends AbstactTest {
    private final RestaurantService restaurantService;
    private final VoteRepository voteRepository;

    @Autowired
    public RestaurantServiceTest(RestaurantService restaurantService, VoteRepository voteRepository) {
        this.restaurantService = restaurantService;
        this.voteRepository = voteRepository;
    }

    @Test
    void getRestaurantsWithMenuByDate() {
        List<Restaurant> expectedRestaurants = restaurantService.getRestaurantsWithMenuByDate(expectedDate);
        List<Restaurant> actualRestaurants = allSaveRestaurants.stream()
                .filter(restaurant -> restaurant.getMenus().stream()
                        .anyMatch(menu -> menu.getDate().isEqual(expectedDate))).collect(Collectors.toList());
        Assertions.assertEquals(expectedRestaurants, actualRestaurants);

    }

    @Test
    void checkIdExists() {
        Assertions.assertTrue(restaurantService.checkIdExists(RESTAURANT_1.getId()));
        Assertions.assertFalse(restaurantService.checkIdExists(2000));
    }

    @Test
    void checkIdExistsAndHasMenuByDate() {
        Assertions.assertTrue(restaurantService.checkIdExistsAndHasMenuByDate(RESTAURANT_1.getId(), expectedDate));
        Assertions.assertFalse(restaurantService.checkIdExistsAndHasMenuByDate(RESTAURANT_3.getId(), expectedDate));
    }

    @Test
    void voteToRestaurant() {
        Assertions.assertTrue(restaurantService.voteToRestaurant(RESTAURANT_1.getId(), USER4.getId(), expectedDate));
        Vote expectedVote = voteRepository.findByUserAndDate(USER1.getId(), expectedDate).orElse(null);
        Assertions.assertNotNull(expectedVote);
        Assertions.assertEquals(expectedVote.getRestaurant().getId(), RESTAURANT_1.getId());
        Assertions.assertEquals(expectedVote.getDate(), expectedDate);
        Assertions.assertEquals(expectedVote.getUser().getId(), USER1.getId());

        Assertions.assertFalse(restaurantService.voteToRestaurant(RESTAURANT_2.getId(), USER1.getId(), expectedDate));
    }


    @Test
    void getAllRestaurants() {
        List<Restaurant> expectedRestaurants = restaurantService.getAllRestaurants();
        List<Restaurant> actualRestaurants = allSaveRestaurants;
        Assertions.assertEquals(expectedRestaurants, actualRestaurants);
    }

    @Test
    void save() {
        Restaurant actualRestaurant = restaurantService.save(RESTAURANT_5_NON_SAVE);
        Assertions.assertFalse(actualRestaurant.isNew());
        Optional<Restaurant> optionalExpectedRestaurant = restaurantRepository.findById(actualRestaurant.getId());
        Assertions.assertTrue(optionalExpectedRestaurant.isPresent());
        Assertions.assertEquals(optionalExpectedRestaurant.get(), actualRestaurant);
    }

    @Test
    void deleteRestaurantById() {
        restaurantService.deleteRestaurantById(RESTAURANT_1.getId());
        Assertions.assertFalse(restaurantRepository.existsById(RESTAURANT_1.getId()));
    }

    @Test
    void getRestaurantsByDate() {
        List<Restaurant> expectedRestaurants = restaurantService.getRestaurantsWithMenuByDate(expectedDate);
        List<Restaurant> actualRestaurants = allSaveRestaurants.stream()
                .filter(restaurant -> restaurant.getMenus().stream()
                        .anyMatch(menu -> menu.getDate().isEqual(expectedDate))).collect(Collectors.toList());
        Assertions.assertEquals(expectedRestaurants, actualRestaurants);
    }
}