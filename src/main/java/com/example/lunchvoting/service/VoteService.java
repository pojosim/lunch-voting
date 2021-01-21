package com.example.lunchvoting.service;

import com.example.lunchvoting.entity.Vote;
import com.example.lunchvoting.repository.RestaurantRepository;
import com.example.lunchvoting.repository.UserRepository;
import com.example.lunchvoting.repository.VoteRepository;
import com.example.lunchvoting.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class VoteService implements com.example.lunchvoting.service.Service {
    public static final LocalTime END_TIME_IS_VOTING = LocalTime.of(11, 0);

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public VoteService(VoteRepository voteRepository, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public int getCountVotesByDate(Integer restaurantId, LocalDate date) {
        return voteRepository.getCountVotesByDate(restaurantId, date);
    }

    @Override
    public boolean checkIdExists(Integer id) {
        return voteRepository.existsById(id);
    }

    public boolean voteToRestaurant(Integer restaurantId, Integer userId, LocalDate date) {
        return voteToRestaurant(restaurantId, userId, date, LocalTime.now());
    }

    public boolean voteToRestaurant(Integer restaurantId, Integer userId, LocalDate date, LocalTime time) {
        ValidationUtil.checkNotFoundWithId(restaurantRepository.existsById(restaurantId), restaurantId);

        if (time.isAfter(END_TIME_IS_VOTING)) {
            return false;
        }

        Optional<Vote> optionalVote = voteRepository.findByUserAndDate(userId, date);
        Vote vote;
        if (optionalVote.isPresent()) {
            if (optionalVote.get().getRestaurant().getId().equals(restaurantId)) {
                return true;
            } else {
                vote = optionalVote.get();
                vote.setRestaurant(restaurantRepository.getOne(restaurantId));
            }
        } else {
            vote = new Vote(date, userRepository.getOne(userId), restaurantRepository.getOne(restaurantId));
        }
        voteRepository.save(vote);
        return !vote.isNew();
    }
}
