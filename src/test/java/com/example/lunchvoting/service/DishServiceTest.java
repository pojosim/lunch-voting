package com.example.lunchvoting.service;

import com.example.lunchvoting.AbstactTest;
import com.example.lunchvoting.entity.Dish;
import com.example.lunchvoting.entity.Menu;
import com.example.lunchvoting.repository.DishRepository;
import com.example.lunchvoting.web.dto.DishTo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.example.lunchvoting.TestData.*;

class DishServiceTest extends AbstactTest {
    private final DishService dishService;
    private final DishRepository dishRepository;

    @Autowired
    public DishServiceTest(DishService dishService, DishRepository dishRepository) {
        this.dishService = dishService;
        this.dishRepository = dishRepository;
    }

    @Test
    void createDishes() {
        Menu menu = RESTAURANT_3.getMenus().get(0);
        List<Dish> saveDishes = dishService.createDishes(List.of(
                new DishTo(new Dish("test dish1", new BigDecimal("100"), menu)),
                new DishTo(new Dish("test dish2", new BigDecimal("200"), menu)),
                new DishTo(new Dish("test dish3", new BigDecimal("12"), menu))
        ), menu);
        saveDishes.forEach(expectedDish -> Assertions.assertFalse(expectedDish.isNew()));
        saveDishes.forEach(expectedDish -> {
            Optional<Dish> findDish = dishRepository.findById(expectedDish.getId());
            Assertions.assertTrue(findDish.isPresent());
            findDish.ifPresent(actualDish -> Assertions.assertEquals(expectedDish, actualDish));
        });
    }

    @Test
    void checkIdExists() {
        Assertions.assertTrue(dishService.checkIdExists(RESTAURANT_1.getMenus().get(0).getDishes().get(0).getId()));
        Assertions.assertFalse(dishService.checkIdExists(2000));
    }
}