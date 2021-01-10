package com.example.lunchvoting.service;

import com.example.lunchvoting.entity.Restaurant;
import com.example.lunchvoting.entity.User;
import com.example.lunchvoting.entity.Vote;
import com.example.lunchvoting.repository.VoteRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class VoteService implements com.example.lunchvoting.service.Service {
    private final VoteRepository voteRepository;

    @PersistenceContext
    EntityManager entityManager;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public int getCountVotesByDate(Integer restaurantId, LocalDate date) {
        return voteRepository.getCountVotesByDate(restaurantId, date);
    }

    @Override
    public boolean checkIdExists(Integer id) {
        return voteRepository.existsById(id);
    }

    public Vote createVoteToRestaurant(Integer restaurantId, LocalDate date, Integer userId) {
        User referenceUser = entityManager.getReference(User.class, userId);
        Restaurant referenceRestaurant = entityManager.getReference(Restaurant.class, restaurantId);
        return voteRepository.save(new Vote(date, referenceUser, referenceRestaurant));
    }

    public void deleteVoteById(Integer voteId) {
        voteRepository.deleteById(voteId);
    }

    public Optional<Vote> findByUserAndDate(int userId, LocalDate date) {
        return voteRepository.findByUserAndDate(userId, date);
    }
}
