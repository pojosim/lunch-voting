package com.example.lunchvoting.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDto {

    private int id;
    private String name;
    private List<DishDto> dishes;
    private int countVotes;
}
