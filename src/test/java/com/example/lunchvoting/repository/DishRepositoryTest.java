package com.example.lunchvoting.repository;

import com.example.lunchvoting.AbstactTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.lunchvoting.TestData.*;

class DishRepositoryTest extends AbstactTest {
    private final DishRepository dishRepository;

    @Autowired
    public DishRepositoryTest(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Test
    void deleteAllDishesByMenuId() {
        dishRepository.deleteAllDishesByMenuId(RESTAURANT_1.getMenus().get(0).getId());
        RESTAURANT_1.getMenus().get(0).getDishes().forEach(dish ->
            Assertions.assertTrue(dishRepository.findById(dish.getId()).isEmpty()));
    }
}