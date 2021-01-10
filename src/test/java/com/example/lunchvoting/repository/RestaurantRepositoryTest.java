package com.example.lunchvoting.repository;

import com.example.lunchvoting.AbstactTest;
import com.example.lunchvoting.entity.Menu;
import com.example.lunchvoting.entity.Restaurant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.lunchvoting.TestData.*;


class RestaurantRepositoryTest extends AbstactTest {
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantRepositoryTest(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Test
    void findRestaurantsByName() {
        Restaurant expectedRestaurant = restaurantRepository.findRestaurantsByName(RESTAURANT_1.getName());
        assertRestaurants(expectedRestaurant, RESTAURANT_1);
    }

    @Test
    void findRestaurantsWithMenuByDate() {
        List<Restaurant> expectedRestaurants = restaurantRepository.findRestaurantsWithMenuByDate(expectedDate);
        List<Restaurant> actualRestaurants = List.of(RESTAURANT_1, RESTAURANT_2);
        Assertions.assertEquals(expectedRestaurants, actualRestaurants);
        expectedRestaurants.forEach(restaurant -> {
            Restaurant findRestaurant = actualRestaurants.stream()
                    .filter(restaurant1 -> restaurant1.getId().equals(restaurant.getId()))
                    .findFirst().get();
            org.assertj.core.api.Assertions.assertThat(restaurant.getMenus()).hasSameElementsAs(filterMenu(findRestaurant.getMenus(), expectedDate));
        });
    }

    @Test
    void findByIdAndMenuDate() {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findByIdAndMenuDate(RESTAURANT_2.getId(), expectedDate);
        Assertions.assertTrue(optionalRestaurant.isPresent());
        assertRestaurants(optionalRestaurant.get(), RESTAURANT_2);
        org.assertj.core.api.Assertions.assertThat(optionalRestaurant.get().getMenus()).hasSameElementsAs(filterMenu(RESTAURANT_2.getMenus(), expectedDate));
    }

    private void assertRestaurants(Restaurant expectedRestaurant, Restaurant actualRestaurant) {
        Assertions.assertEquals(expectedRestaurant.getName(), actualRestaurant.getName());
    }

    private List<Menu> filterMenu(List<Menu> menus, LocalDate date) {
        return menus.stream().filter(menu -> menu.getDate().isEqual(date)).collect(Collectors.toList());
    }
}