package com.example.lunchvoting.web.controller.rest;

import com.example.lunchvoting.entity.Menu;
import com.example.lunchvoting.repository.MenuRepository;
import com.example.lunchvoting.web.dto.DishTo;
import com.example.lunchvoting.web.json.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import static com.example.lunchvoting.TestData.*;
import static com.example.lunchvoting.TestUtil.userHttpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuControllerTest extends AbstractControllerTest {
    private final String rest_menu_url = RestaurantController.REST_URL_ADMIN + "/%s/menu";

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void getAllMenuByRestaurant() throws Exception {
        perform(get(String.format(rest_menu_url, RESTAURANT_1.getId()))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.writeValue(RESTAURANT_1.getMenus())));
    }

    @Test
    void getMenuUnauthorized() throws Exception {
        perform(get(String.format(rest_menu_url, RESTAURANT_1.getId())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMenuForbidden() throws Exception {
        perform(get(String.format(rest_menu_url, RESTAURANT_1.getId()))
                .with(userHttpBasic(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createMenuWithDishes() throws Exception {
        Menu menu = getTestingMenu(expectedDate, RESTAURANT_3);

        perform(post(String.format(rest_menu_url, RESTAURANT_3.getId()))
                .param("date", expectedDate.format(DateTimeFormatter.ISO_DATE))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(menu.getDishes().stream().map(DishTo::new).collect(Collectors.toList()))))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json(JsonUtil.writeValue(menu)));
    }

    @Test
    void createMenuNotFoundRestaurant() throws Exception {
        Menu menu = getTestingMenu(expectedDate, RESTAURANT_2);

        perform(post(String.format(rest_menu_url, 2000))
                .param("date", expectedDate.format(DateTimeFormatter.ISO_DATE))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(menu.getDishes().stream().map(DishTo::new).collect(Collectors.toList()))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createMenuConflict() throws Exception {
        Menu menu = getTestingMenu(expectedDate, RESTAURANT_1);

        perform(post(String.format(rest_menu_url, RESTAURANT_1.getId()))
                .param("date", expectedDate.format(DateTimeFormatter.ISO_DATE))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(menu.getDishes().stream().map(DishTo::new).collect(Collectors.toList()))))
                .andExpect(status().isConflict());
    }

    @Test
    void updateMenuWithDishes() throws Exception {
        Menu newMenu = getTestingMenu(expectedDate, RESTAURANT_1);

        perform(put(String.format(rest_menu_url, RESTAURANT_1.getId()))
                .param("date", expectedDate.format(DateTimeFormatter.ISO_DATE))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(newMenu.getDishes().stream().map(DishTo::new).collect(Collectors.toList()))))
                .andExpect(status().isCreated())
                .andExpect(content().json(JsonUtil.writeValue(newMenu)));
    }

    @Test
    void updateMenuWhenRestaurantNotFound() throws Exception {
        Menu newMenu = getTestingMenu(expectedDate, RESTAURANT_4_NON_SAVE);

        perform(put(String.format(rest_menu_url, 2000))
                .param("date", expectedDate.format(DateTimeFormatter.ISO_DATE))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(newMenu.getDishes().stream().map(DishTo::new).collect(Collectors.toList()))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateMenuWhenMenuByDateNotFound() throws Exception {
        Menu newMenu = getTestingMenu(expectedDate, RESTAURANT_3);

        perform(put(String.format(rest_menu_url, RESTAURANT_3.getId()))
                .param("date", expectedDate.format(DateTimeFormatter.ISO_DATE))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(newMenu.getDishes().stream().map(DishTo::new).collect(Collectors.toList()))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deleteMenu() throws Exception {
        perform(delete(String.format(rest_menu_url, RESTAURANT_1.getId()))
                .param("date", expectedDate.format(DateTimeFormatter.ISO_DATE))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());

        Assertions.assertNull(menuRepository.findMenuByRestaurantAndDate(RESTAURANT_1.getId(), expectedDate));
    }

    @Test
    void deleteMenuForbidden() throws Exception {
        perform(delete(String.format(rest_menu_url, RESTAURANT_1.getId())).with(userHttpBasic(USER1)))
                .andExpect(status().isForbidden());

    }
}