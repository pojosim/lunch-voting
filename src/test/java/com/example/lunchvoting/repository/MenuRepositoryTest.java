package com.example.lunchvoting.repository;

import com.example.lunchvoting.AbstactTest;
import com.example.lunchvoting.entity.Menu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.example.lunchvoting.TestData.*;

public class MenuRepositoryTest extends AbstactTest {
    private final MenuRepository menuRepository;

    @Autowired
    public MenuRepositoryTest(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Test
    void findMenusByRestaurantId() {
        List<Menu> actualMenus = RESTAURANT_1.getMenus();
        List<Menu> expectedMenus = menuRepository.findMenusByRestaurantId(RESTAURANT_1.getId());
        Assertions.assertEquals(expectedMenus, actualMenus);
    }

    @Test
    void findMenuByRestaurantAndDate() {
        Menu expectedMenu = menuRepository.findMenuByRestaurantAndDate(RESTAURANT_2.getId(), expectedDate);
        Assertions.assertEquals(expectedMenu, RESTAURANT_2.getMenus().stream().filter(menu -> menu.getDate().isEqual(expectedDate)).findFirst().get());
    }

    @Test
    void deleteByRestaurantIdAndDate() {
        menuRepository.deleteByRestaurantIdAndDate(RESTAURANT_2.getId(), expectedDate);
        Assertions.assertTrue(menuRepository.findById(RESTAURANT_2.getMenus().stream()
                .filter(menu -> menu.getDate().isEqual(expectedDate))
                .findFirst().get().getId())
                .isEmpty());
    }
}
