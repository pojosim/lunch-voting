package com.example.lunchvoting.web.dto;

import com.example.lunchvoting.entity.Dish;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class DishTo {

    private String name;
    private BigDecimal price;

    public DishTo(Dish dish) {
        name = dish.getName();
        price = dish.getPrice();
    }
}
