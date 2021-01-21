package com.example.lunchvoting.service;

import com.example.lunchvoting.AbstractTest;
import com.example.lunchvoting.TestData;
import com.example.lunchvoting.entity.Dish;
import com.example.lunchvoting.entity.Menu;
import com.example.lunchvoting.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.example.lunchvoting.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

class DishServiceTest extends AbstractTest {

    private final DishService dishService;

    @Autowired
    public DishServiceTest(DishService dishService) {
        this.dishService = dishService;
    }

    @Test
    void checkIdExists() {
        assertTrue(dishService.checkIdExists(getTodayMenu(RESTAURANT_1).getDishes().get(0).getId()));
        assertFalse(dishService.checkIdExists(2000));
    }
}