package com.example.lunchvoting.service;

import com.example.lunchvoting.entity.Restaurant;
import com.example.lunchvoting.entity.Vote;
import com.example.lunchvoting.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService implements com.example.lunchvoting.service.Service {
    private static final LocalTime endTimeIsVoting = LocalTime.of(11, 0);

    private final RestaurantRepository restaurantRepository;
    private final VoteService voteService;

    public RestaurantService(RestaurantRepository restaurantRepository, VoteService voteService) {
        this.restaurantRepository = restaurantRepository;
        this.voteService = voteService;
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

    public boolean voteToRestaurant(Integer restaurantId, Integer userId, LocalDate date) {
        Optional<Vote> optionalVote = voteService.findByUserAndDate(userId, date);
        if (optionalVote.isPresent()) {
            LocalTime nowTime = LocalTime.now();
            if (!optionalVote.get().getRestaurant().getId().equals(restaurantId) || nowTime.isAfter(endTimeIsVoting)) {
                return false;
            } else {
                voteService.deleteVoteById(optionalVote.get().getId());
            }
        }

        Vote vote = voteService.createVoteToRestaurant(restaurantId, date, userId);
        return !vote.isNew();
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
