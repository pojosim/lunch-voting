package com.example.lunchvoting.service;

import com.example.lunchvoting.AbstactTest;
import com.example.lunchvoting.entity.Vote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.lunchvoting.TestData.*;

class VoteServiceTest extends AbstactTest {
    private final VoteService voteService;

    @Autowired
    public VoteServiceTest(VoteService voteService) {
        this.voteService = voteService;
    }

    @Test
    void getCountVotesByDate() {
        int expectedCountVote = voteService.getCountVotesByDate(RESTAURANT_1.getId(), expectedDate);
        Integer actualCountVote = mapRestaurantIdToCountVote.get(RESTAURANT_1.getId());
        Assertions.assertEquals(expectedCountVote, actualCountVote);
    }

    @Test
    void checkIdExists() {
        Assertions.assertTrue(voteService.checkIdExists(VOTE1.getId()));
        Assertions.assertFalse(voteService.checkIdExists(100002));
    }

    @Test
    void createVoteToRestaurant() {
        Vote expectedVote = voteService.createVoteToRestaurant(RESTAURANT_3.getId(), expectedDate, USER4.getId());
        Vote actualVote = voteRepository.findById(expectedVote.getId()).orElse(null);
        Assertions.assertNotNull(actualVote);
        Assertions.assertEquals(expectedVote, actualVote);
    }

    @Test
    void deleteVoteById() {
        voteService.deleteVoteById(VOTE2.getId());
        Assertions.assertFalse(voteRepository.existsById(VOTE2.getId()));
    }

    @Test
    void findByUserAndDate() {
        Vote expectedVote = voteService.findByUserAndDate(USER3.getId(), expectedDate).orElse(null);
        Assertions.assertNotNull(expectedVote);
        Assertions.assertEquals(expectedVote, VOTE3);
    }
}