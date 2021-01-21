package com.example.lunchvoting.service;

import com.example.lunchvoting.entity.Restaurant;
import com.example.lunchvoting.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RestaurantService implements com.example.lunchvoting.service.Service {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> getRestaurantsWithMenuByDate(LocalDate date) {
        return restaurantRepository.findRestaurantsWithMenuByDate(date);
    }

    @Override
    public boolean checkIdExists(Integer id) {
        return restaurantRepository.existsById(id);
    }

    public boolean checkIdExistsAndHasMenuByDate(Integer restaurantId, LocalDate nowDate) {
        return restaurantRepository.findByIdAndMenuDate(restaurantId, nowDate).isPresent();
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public void deleteRestaurantById(Integer restaurantId) {
        restaurantRepository.deleteById(restaurantId);
    }

    public List<Restaurant> getRestaurantsByDate(LocalDate date) {
        return restaurantRepository.findRestaurantsWithMenuByDate(date);
    }
}
