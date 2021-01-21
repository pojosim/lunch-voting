package com.example.lunchvoting.repository;

import com.example.lunchvoting.AbstractTest;
import com.example.lunchvoting.entity.Menu;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.example.lunchvoting.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

public class MenuRepositoryTest extends AbstractTest {

    @Test
    void save() {
        LocalDate date = TODAY_DATE.plusDays(1);
        Menu expectedMenu = new Menu(date, RESTAURANT_2);
        menuRepository.save(expectedMenu);
        Menu actualMenu = menuRepository.findById(expectedMenu.getId()).orElse(null);
        assertNotNull(actualMenu);
        assertMatchMenu(expectedMenu, actualMenu);

        Menu actualMenuByDate = menuRepository.findMenuByRestaurantAndDate(RESTAURANT_2.getId(), date).orElse(null);
        assertNotNull(actualMenuByDate);
        assertMatchMenu(expectedMenu, actualMenuByDate);
    }

    @Test
    void saveWithDishes() {
        LocalDate date = TODAY_DATE.plusDays(1);
        Menu expectedMenu = createNewMenu(date, RESTAURANT_2);
        menuRepository.save(expectedMenu);
        Menu actualMenu = menuRepository.findById(expectedMenu.getId()).orElse(null);
        assertNotNull(actualMenu);
        assertMatchMenu(expectedMenu, actualMenu);

        Menu actualMenuByDate = menuRepository.findMenuByRestaurantAndDate(RESTAURANT_2.getId(), date).orElse(null);
        assertNotNull(actualMenuByDate);
        assertMatchMenu(expectedMenu, actualMenuByDate);
    }

    @Test
    void findMenusByRestaurantId() {
        List<Menu> expectedMenus = RESTAURANT_1.getMenus();
        List<Menu> actualMenus = menuRepository.findMenusByRestaurantId(RESTAURANT_1.getId());
        assertNotNull(actualMenus);
        assertMatchMenus(expectedMenus, actualMenus);
    }

    @Test
    void findMenuByRestaurantAndDate() {
        Menu expectedMenu = getTodayMenu(RESTAURANT_1);
        Menu actualMenu = menuRepository.findMenuByRestaurantAndDate(RESTAURANT_1.getId(), TODAY_DATE).orElse(null);
        assertNotNull(actualMenu);
        assertMatchMenu(expectedMenu, actualMenu);
    }

    @Test
    void deleteByRestaurantIdAndDate() {
        Menu expectedMenu = getTodayMenu(RESTAURANT_1);
        menuRepository.deleteByRestaurantIdAndDate(RESTAURANT_1.getId(), TODAY_DATE);
        Menu actualMenu = menuRepository.findById(expectedMenu.getId()).orElse(null);
        assertNull(actualMenu);
    }
}
