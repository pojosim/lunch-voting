package com.example.lunchvoting.service;

import com.example.lunchvoting.AbstractTest;
import com.example.lunchvoting.entity.Dish;
import com.example.lunchvoting.entity.Menu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static com.example.lunchvoting.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

class MenuServiceTest extends AbstractTest {
    private final MenuService menuService;

    @Autowired
    public MenuServiceTest(MenuService menuService) {
        this.menuService = menuService;
    }

    @Test
    void save() {
        LocalDate date = TODAY_DATE;
        List<Dish> expectedDishes = createNewDishes();
        menuService.saveOrUpdate(RESTAURANT_3.getId(), expectedDishes, date);
        Menu actualMenu = menuRepository.findMenuByRestaurantAndDate(RESTAURANT_3.getId(), date).orElse(null);
        assertNotNull(actualMenu);
        assertNotNull(actualMenu.getDishes());
        assertMatchDishes(expectedDishes, actualMenu.getDishes());
    }

    @Test
    void update() {
        List<Dish> expectedDishes = createNewDishes();
        menuService.saveOrUpdate(RESTAURANT_1.getId(), expectedDishes, TODAY_DATE);
        Menu actualMenu = menuRepository.findMenuByRestaurantAndDate(RESTAURANT_1.getId(), TODAY_DATE).orElse(null);
        assertNotNull(actualMenu);
        assertNotNull(actualMenu.getDishes());
        assertMatchDishes(expectedDishes, actualMenu.getDishes(), "id");
    }

    @Test
    void delete() {
        menuService.delete(RESTAURANT_1.getId(), TODAY_DATE);
        assertFalse(menuService.checkDateIsExists(TODAY_DATE, RESTAURANT_1.getId()));
    }

    @Test
    void checkIdExists() {
        assertTrue(menuService.checkIdExists(getTodayMenu(RESTAURANT_1).getId()));
        assertFalse(menuService.checkIdExists(2000));
    }

    @Test
    void checkDateIsExists() {
        assertTrue(menuService.checkDateIsExists(TODAY_DATE, RESTAURANT_1.getId()));
        assertFalse(menuService.checkDateIsExists(TODAY_DATE, 100000));
    }
}