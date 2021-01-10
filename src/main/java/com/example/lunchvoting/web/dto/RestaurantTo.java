package com.example.lunchvoting.web.dto;

import com.example.lunchvoting.entity.Restaurant;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RestaurantTo {

    private final String name;
    private final List<DishTo> dishes;
    private final int countVotes;

    public RestaurantTo(Restaurant restaurant, int countVotes) {
        name = restaurant.getName();
        this.countVotes = countVotes;
        if (restaurant.getMenus() != null && !restaurant.getMenus().isEmpty()) {
            dishes = restaurant.getMenus().get(0).getDishes().stream().map(DishTo::new).collect(Collectors.toList());
        } else {
            dishes = Collections.emptyList();
        }
    }
}
