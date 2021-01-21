package com.example.lunchvoting.util;

import com.example.lunchvoting.entity.Dish;
import com.example.lunchvoting.entity.Menu;
import com.example.lunchvoting.entity.Restaurant;
import com.example.lunchvoting.web.dto.DishDto;
import com.example.lunchvoting.web.dto.RestaurantDto;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DtoUtil {

    public static DishDto createDishDto(Dish dish) {
        return new DishDto(dish.getName(), dish.getPrice());
    }

    public static Dish createDish(DishDto dishDto, Menu menu) {
        return new Dish(dishDto.getName(), dishDto.getPrice(), menu);
    }

    public static Dish createDish(DishDto dishDto) {
        return new Dish(dishDto.getName(), dishDto.getPrice());
    }

    public static RestaurantDto createRestaurantDto(Restaurant restaurant, int countVotes) {
        return createRestaurantDto(restaurant, null, countVotes);
    }

    public static RestaurantDto createRestaurantDto(Restaurant restaurant, @Nullable Menu menu, int countVotes) {
        List<DishDto> dishDtoList;
        if (Objects.nonNull(menu)) {
            dishDtoList = menu.getDishes().stream().map(DtoUtil::createDishDto).collect(Collectors.toList());
        } else {
            dishDtoList = Collections.emptyList();
        }
        return new RestaurantDto(restaurant.getId(), restaurant.getName(), dishDtoList, countVotes);
    }

    public static Restaurant createRestaurant(RestaurantDto restaurantDto) {
        return new Restaurant(restaurantDto.getId(), restaurantDto.getName());
    }

    public static List<Dish> convertDtosToDishes(List<DishDto> dishDtoList, Menu menu) {
        return dishDtoList.stream().map(dishDto -> createDish(dishDto, menu)).collect(Collectors.toList());
    }

    public static List<Dish> convertDtosToDishes(List<DishDto> dishDtoList) {
        return dishDtoList.stream().map(dishDto -> createDish(dishDto)).collect(Collectors.toList());
    }

    public static List<DishDto> convertDishesToDtos(List<Dish> dishList) {
        return dishList.stream().map(DtoUtil::createDishDto).collect(Collectors.toList());
    }
}
