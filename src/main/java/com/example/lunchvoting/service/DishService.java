package com.example.lunchvoting.service;

import com.example.lunchvoting.entity.Dish;
import com.example.lunchvoting.entity.Menu;
import com.example.lunchvoting.repository.DishRepository;
import com.example.lunchvoting.web.dto.DishTo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishService implements com.example.lunchvoting.service.Service {
    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<Dish> createDishes(List<DishTo> dishes, Menu menu) {
        return dishes.stream().map(dishTo -> dishRepository.save(new Dish(dishTo, menu)))
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkIdExists(Integer id) {
        return dishRepository.existsById(id);
    }
}
