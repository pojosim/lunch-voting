package com.example.lunchvoting.web.controller.rest;

import com.example.lunchvoting.entity.Menu;
import com.example.lunchvoting.repository.MenuRepository;
import com.example.lunchvoting.util.DtoUtil;
import com.example.lunchvoting.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import static com.example.lunchvoting.TestData.*;
import static com.example.lunchvoting.TestUtil.userHttpBasic;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuControllerTest extends AbstractControllerTest {
    public static final String REST_MENU_URL = RestaurantController.REST_URL_ADMIN + "/%s/menu";

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void getAllMenuByRestaurant() throws Exception {
        perform(get(String.format(REST_MENU_URL, RESTAURANT_1.getId()))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.writeValue(RESTAURANT_1.getMenus())));
    }

    @Test
    void getMenuUnauthorized() throws Exception {
        perform(get(String.format(REST_MENU_URL, RESTAURANT_1.getId())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMenuForbidden() throws Exception {
        perform(get(String.format(REST_MENU_URL, RESTAURANT_1.getId()))
                .with(userHttpBasic(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createMenuByUserRoleIsForbidden() throws Exception {
        Menu menu = createNewMenu(TODAY_DATE, RESTAURANT_3);

        perform(post(String.format(REST_MENU_URL, RESTAURANT_3.getId()))
                .param("date", TODAY_DATE.format(DateTimeFormatter.ISO_DATE))
                .with(userHttpBasic(USER1))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(menu.getDishes().stream().map(DtoUtil::createDishDto).collect(Collectors.toList()))))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateMenuByUserRoleIsForbidden() throws Exception {
        Menu menu = createNewMenu(TODAY_DATE, RESTAURANT_3);

        perform(put(String.format(REST_MENU_URL, RESTAURANT_3.getId()))
                .param("date", TODAY_DATE.format(DateTimeFormatter.ISO_DATE))
                .with(userHttpBasic(USER1))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(menu.getDishes().stream().map(DtoUtil::createDishDto).collect(Collectors.toList()))))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteMenuByUserRoleIsForbidden() throws Exception {
        perform(delete(String.format(REST_MENU_URL, RESTAURANT_1.getId())).with(userHttpBasic(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createMenu() throws Exception {
        Menu newMenu = createNewMenu(TODAY_DATE, RESTAURANT_3);

        perform(post(String.format(REST_MENU_URL, RESTAURANT_3.getId()))
                .param("date", TODAY_DATE.format(DateTimeFormatter.ISO_DATE))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(newMenu.getDishes().stream().map(DtoUtil::createDishDto).collect(Collectors.toList()))))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json(JsonUtil.writeValue(newMenu)));
    }

    @Test
    void updateMenu() throws Exception {
        Menu newMenu = createNewMenu(TODAY_DATE, RESTAURANT_2);

        perform(put(String.format(REST_MENU_URL, RESTAURANT_2.getId()))
                .param("date", TODAY_DATE.format(DateTimeFormatter.ISO_DATE))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(newMenu.getDishes().stream().map(DtoUtil::createDishDto).collect(Collectors.toList()))))
                .andExpect(status().isCreated())
                .andExpect(content().json(JsonUtil.writeValue(newMenu)));
    }

    @Test
    void deleteMenu() throws Exception {
        perform(delete(String.format(REST_MENU_URL, RESTAURANT_1.getId()))
                .param("date", TODAY_DATE.format(DateTimeFormatter.ISO_DATE))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());

        assertTrue(menuRepository.findMenuByRestaurantAndDate(RESTAURANT_1.getId(), TODAY_DATE).isEmpty());
    }

    @Test
    void createMenuWhenRestaurantNotFound() throws Exception {
        Menu newMenu = createNewMenu(TODAY_DATE, createNewRestaurant(TODAY_DATE));

        perform(put(String.format(REST_MENU_URL, 2000))
                .param("date", TODAY_DATE.format(DateTimeFormatter.ISO_DATE))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(newMenu.getDishes().stream().map(DtoUtil::createDishDto).collect(Collectors.toList()))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateMenuWhenRestaurantNotFound() throws Exception {
        Menu newMenu = createNewMenu(TODAY_DATE, createNewRestaurant(TODAY_DATE));

        perform(put(String.format(REST_MENU_URL, 2000))
                .param("date", TODAY_DATE.format(DateTimeFormatter.ISO_DATE))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(newMenu.getDishes().stream().map(DtoUtil::createDishDto).collect(Collectors.toList()))))
                .andExpect(status().isUnprocessableEntity());
    }
}