package com.example.lunchvoting.repository;

import com.example.lunchvoting.AbstractTest;
import com.example.lunchvoting.TestData;
import com.example.lunchvoting.TestUtil;
import com.example.lunchvoting.entity.Dish;
import com.example.lunchvoting.entity.Menu;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.example.lunchvoting.TestData.*;
import static com.example.lunchvoting.TestData.RESTAURANT_1;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DishRepositoryTest extends AbstractTest {

    @Test
    void save() {
        Menu menu = getTodayMenu(RESTAURANT_1);
        Dish expectedDish = new Dish("test dish", new BigDecimal("176.00"), menu);
        dishRepository.save(expectedDish);
        Dish actualDish = dishRepository.findById(expectedDish.getId()).orElse(null);
        assertNotNull(actualDish);
        assertMatchDish(expectedDish, actualDish);
    }

    @Test
    void deleteAllDishesByMenuId() {
        Menu menu = findMenuByNowDate(RESTAURANT_1.getMenus());
        dishRepository.deleteAllDishesByMenuId(menu.getId());
        menu.getDishes().forEach(dish -> assertTrue(dishRepository.findById(dish.getId()).isEmpty()));
    }
}