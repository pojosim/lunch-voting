package com.example.lunchvoting.web.controller.rest;

import com.example.lunchvoting.entity.Menu;
import com.example.lunchvoting.repository.MenuRepository;
import com.example.lunchvoting.service.MenuService;
import com.example.lunchvoting.service.RestaurantService;
import com.example.lunchvoting.util.exception.NotFoundException;
import com.example.lunchvoting.web.dto.DishDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.example.lunchvoting.util.DtoUtil.convertDtosToDishes;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController {
    private static final Logger log = LoggerFactory.getLogger(MenuController.class);
    public static final String REST_URL_ADMIN = RestaurantController.REST_URL_ADMIN + "/{id}/menu";

    private final RestaurantService restaurantService;
    private final MenuService menuService;
    private final MenuRepository menuRepository;

    public MenuController(RestaurantService restaurantService, MenuService menuService, MenuRepository menuRepository) {
        this.restaurantService = restaurantService;
        this.menuService = menuService;
        this.menuRepository = menuRepository;
    }

    @GetMapping(value = REST_URL_ADMIN)
    public List<Menu> getAllMenuByRestaurant(@PathVariable("id") Integer restaurantId) {
        Objects.requireNonNull(restaurantId);
        return menuRepository.findMenusByRestaurantId(restaurantId);
    }

    @PostMapping(value = REST_URL_ADMIN, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createMenuWithDishes(@PathVariable("id") Integer restaurantId,
                                                     @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                     @RequestBody List<DishDto> dishDtoList) {
        if (!restaurantService.checkIdExists(restaurantId))
            throw new NotFoundException("Not found restaurant " + restaurantId);
        else if (menuService.checkDateIsExists(date, restaurantId))
            throw new DataIntegrityViolationException("Conflict create menu restaurant " + restaurantId);

        log.debug("create menu to restaurant {}, is date {}, dishes {}", restaurantId, date, dishDtoList);
        Menu menu = menuService.saveOrUpdate(restaurantId, convertDtosToDishes(dishDtoList), date);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL_ADMIN)
                .buildAndExpand(restaurantId).toUri();
        return ResponseEntity.created(uriOfNewResource).body(menu);
    }

    @PutMapping(value = REST_URL_ADMIN, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> updateMenuWithDishes(@PathVariable("id") Integer restaurantId,
                                                     @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                     @RequestBody List<DishDto> dishDtoList) {
        if (!restaurantService.checkIdExists(restaurantId))
            throw new NotFoundException("Not found restaurant " + restaurantId);
        if (!menuService.checkDateIsExists(date, restaurantId))
            throw new NotFoundException("Not found menu " + restaurantId);

        log.debug("update menu to restaurant {}, new dishes {}", restaurantId, dishDtoList);
        Menu menu = menuService.saveOrUpdate(restaurantId, convertDtosToDishes(dishDtoList), date);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL_ADMIN)
                .buildAndExpand(restaurantId).toUri();
        return ResponseEntity.created(uriOfNewResource).body(menu);
    }

    @DeleteMapping(REST_URL_ADMIN)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Integer restaurantId, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Objects.requireNonNull(restaurantId);
        log.debug("delete menu date {}, from restaurant {} ", date, restaurantId);
        menuRepository.deleteByRestaurantIdAndDate(restaurantId, date);
    }
}
