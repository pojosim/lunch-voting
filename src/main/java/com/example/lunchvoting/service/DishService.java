package com.example.lunchvoting.service;

import com.example.lunchvoting.repository.DishRepository;
import org.springframework.stereotype.Service;

@Service
public class DishService implements com.example.lunchvoting.service.Service {

    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Override
    public boolean checkIdExists(Integer id) {
        return dishRepository.existsById(id);
    }
}
