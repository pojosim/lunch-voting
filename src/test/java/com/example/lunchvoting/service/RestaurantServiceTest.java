package com.example.lunchvoting.service;

import com.example.lunchvoting.AbstractTest;
import com.example.lunchvoting.entity.Restaurant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.lunchvoting.TestData.*;

class RestaurantServiceTest extends AbstractTest {
    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantServiceTest(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Test
    void getRestaurantsWithMenuByDate() {
        List<Restaurant> actualRestaurants = restaurantService.getRestaurantsWithMenuByDate(TODAY_DATE);

        List<Restaurant> expectedRestaurants = allSavedRestaurants.stream()
                .filter(restaurant -> restaurant.getMenus().stream()
                        .anyMatch(menu -> menu.getDate().isEqual(TODAY_DATE))).collect(Collectors.toList());
        org.assertj.core.api.Assertions.assertThat(actualRestaurants).hasSameElementsAs(expectedRestaurants);
    }

    @Test
    void checkIdExists() {
        Assertions.assertTrue(restaurantService.checkIdExists(RESTAURANT_1.getId()));
        Assertions.assertFalse(restaurantService.checkIdExists(2000));
    }

    @Test
    void checkIdExistsAndHasMenuByDate() {
        Assertions.assertTrue(restaurantService.checkIdExistsAndHasMenuByDate(RESTAURANT_1.getId(), TODAY_DATE));
        Assertions.assertFalse(restaurantService.checkIdExistsAndHasMenuByDate(100000, TODAY_DATE));
    }

    @Test
    void getAllRestaurants() {
        List<Restaurant> actualRestaurants = restaurantService.getAllRestaurants();
        org.assertj.core.api.Assertions.assertThat(actualRestaurants).hasSameElementsAs(actualRestaurants);
    }

    @Test
    void save() {
        Restaurant expectedRestaurant = createNewRestaurant(TODAY_DATE) ;
        restaurantService.save(expectedRestaurant);
        Restaurant actualRestaurant = restaurantRepository.findById(expectedRestaurant.getId()).orElse(null);
        Assertions.assertNotNull(actualRestaurant);
        assertMatchRestaurants(expectedRestaurant, actualRestaurant);
    }

    @Test
    void deleteRestaurantById() {
        restaurantService.deleteRestaurantById(RESTAURANT_1.getId());
        Assertions.assertFalse(restaurantRepository.existsById(RESTAURANT_1.getId()));
    }

    @Test
    void getRestaurantsByDate() {
        List<Restaurant> expectedRestaurants = restaurantService.getRestaurantsWithMenuByDate(TODAY_DATE);
        List<Restaurant> actualRestaurants = allSavedRestaurants.stream()
                .filter(restaurant -> restaurant.getMenus().stream()
                        .anyMatch(menu -> menu.getDate().isEqual(TODAY_DATE))).collect(Collectors.toList());
        Assertions.assertEquals(expectedRestaurants, actualRestaurants);
    }
}