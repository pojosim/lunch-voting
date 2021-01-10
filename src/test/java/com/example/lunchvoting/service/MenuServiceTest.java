package com.example.lunchvoting.service;

import com.example.lunchvoting.AbstactTest;
import com.example.lunchvoting.entity.Menu;
import com.example.lunchvoting.repository.MenuRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static com.example.lunchvoting.TestData.*;

class MenuServiceTest extends AbstactTest {
    private final MenuService menuService;
    private final MenuRepository menuRepository;

    @Autowired
    public MenuServiceTest(MenuService menuService, MenuRepository menuRepository) {
        this.menuService = menuService;
        this.menuRepository = menuRepository;
    }

    @Test
    void createMenu() {
        Menu actualMenu = menuService.createMenu(RESTAURANT_1.getId(), LocalDate.of(2021, Month.DECEMBER, 31));
        Assertions.assertFalse(actualMenu.isNew());
        Optional<Menu> optionalExpectedMenu = menuRepository.findById(actualMenu.getId());
        Assertions.assertTrue(optionalExpectedMenu.isPresent());
        optionalExpectedMenu.ifPresent(expectedMenu -> Assertions.assertEquals(expectedMenu, actualMenu));
    }

    @Test
    void checkIdExists() {
        Assertions.assertTrue(menuService.checkIdExists(RESTAURANT_1.getMenus().get(0).getId()));
        Assertions.assertFalse(menuService.checkIdExists(2000));
    }

    @Test
    void checkDateIsExists() {
        Assertions.assertTrue(menuService.checkDateIsExists(expectedDate, RESTAURANT_1.getId()));
        Assertions.assertFalse(menuService.checkDateIsExists(expectedDate, RESTAURANT_3.getId()));
    }
}