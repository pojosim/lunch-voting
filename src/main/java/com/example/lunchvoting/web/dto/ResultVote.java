package com.example.lunchvoting.web.dto;

import com.example.lunchvoting.entity.Restaurant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultVote {
    String restaurantName;
    int countVote;

    public ResultVote(Restaurant restaurant, int countVote) {
        restaurantName = restaurant.getName();
        this.countVote = countVote;
    }
}
