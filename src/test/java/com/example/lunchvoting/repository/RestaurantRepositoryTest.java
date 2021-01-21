package com.example.lunchvoting.repository;

import com.example.lunchvoting.AbstractTest;
import com.example.lunchvoting.TestData;
import com.example.lunchvoting.entity.Restaurant;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.lunchvoting.TestData.*;
import static org.junit.jupiter.api.Assertions.*;


class RestaurantRepositoryTest extends AbstractTest {

    @Test
    void save() {
        Restaurant expectedRestaurant = createNewRestaurant(TODAY_DATE);
        restaurantRepository.save(expectedRestaurant);
        Restaurant actualRestaurant = restaurantRepository.findById(expectedRestaurant.getId()).orElse(null);
        assertNotNull(actualRestaurant);
        assertMatchRestaurants(expectedRestaurant, actualRestaurant);
    }

    @Test
    void findRestaurantsByName() {
        Restaurant actualRestaurant = restaurantRepository.findRestaurantsByName(RESTAURANT_1.getName());
        assertNotNull(actualRestaurant);
        assertMatchRestaurants(RESTAURANT_1, actualRestaurant);
    }

    @Test
    void findRestaurantsWithMenuByDate() {
        List<Restaurant> expectedRestaurants = TestData.allSavedRestaurants.stream()
                .filter(restaurant -> restaurant.getMenus().stream().anyMatch(menu -> menu.getDate().isEqual(TODAY_DATE)))
                .collect(Collectors.toList());
        List<Restaurant> actualRestaurants = restaurantRepository.findRestaurantsWithMenuByDate(TODAY_DATE);
        assertEquals(expectedRestaurants, actualRestaurants);

        actualRestaurants.forEach(actualRestaurant -> {
            Restaurant expectedRestaurant = expectedRestaurants.stream()
                    .filter(restaurant -> restaurant.getId().equals(actualRestaurant.getId()))
                    .findFirst().orElse(null);
            assertNotNull(expectedRestaurant);
            assertNotNull(expectedRestaurant.getMenus());
            assertEquals(actualRestaurant.getMenus().size(), 1);
            assertMatchMenu(findMenuByDate(expectedRestaurant.getMenus(), TODAY_DATE), actualRestaurant.getMenus().get(0));
        });
    }

    @Test
    void findByIdAndMenuDate() {
        Restaurant actualRestaurant = restaurantRepository.findByIdAndMenuDate(RESTAURANT_2.getId(), TODAY_DATE).orElse(null);
        assertNotNull(actualRestaurant);
        assertMatchRestaurants(RESTAURANT_2, actualRestaurant);
        assertNotNull(actualRestaurant.getMenus());
        assertEquals(actualRestaurant.getMenus().size(), 1);
        assertMatchMenu(findMenuByDate(RESTAURANT_2.getMenus(), TODAY_DATE), actualRestaurant.getMenus().get(0));
    }


}