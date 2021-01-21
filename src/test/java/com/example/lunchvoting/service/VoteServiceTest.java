package com.example.lunchvoting.service;

import com.example.lunchvoting.AbstractTest;
import com.example.lunchvoting.entity.Vote;
import com.example.lunchvoting.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.Optional;

import static com.example.lunchvoting.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

class VoteServiceTest extends AbstractTest {
    public static final LocalTime BEFORE_END_TIME_IS_VOTING = VoteService.END_TIME_IS_VOTING.minusHours(1);
    public static final LocalTime AFTER_END_TIME_IS_VOTING = VoteService.END_TIME_IS_VOTING.plusHours(1);

    private final VoteService voteService;

    @Autowired
    public VoteServiceTest(VoteService voteService) {
        this.voteService = voteService;
    }

    @Test
    void getCountVotesByDate() {
        int expectedCountVote = voteService.getCountVotesByDate(RESTAURANT_1.getId(), TODAY_DATE);
        Integer actualCountVote = mapRestaurantIdToCountVote.get(RESTAURANT_1.getId());
        assertEquals(expectedCountVote, actualCountVote);
    }

    @Test
    void checkIdExists() {
        assertTrue(voteService.checkIdExists(VOTE1.getId()));
        assertFalse(voteService.checkIdExists(100002));
    }

    @Test
    void voteBeforeEndTime() {
        assertTrue(voteService.voteToRestaurant(RESTAURANT_1.getId(), USER4.getId(), TODAY_DATE, BEFORE_END_TIME_IS_VOTING));
        assertTrue(voteRepository.findByUserAndDate(USER1.getId(), TODAY_DATE).isPresent());
    }

    @Test
    void voteAfterEndTime() {
        assertFalse(voteService.voteToRestaurant(RESTAURANT_2.getId(), USER5.getId(), TODAY_DATE, AFTER_END_TIME_IS_VOTING));
        assertTrue(voteRepository.findByUserAndDate(USER5.getId(), TODAY_DATE).isEmpty());
    }

    @Test
    void voteToWrongRestaurant() {
        assertThrows(NotFoundException.class, () -> voteService.voteToRestaurant(2000, USER5.getId(), TODAY_DATE));
    }

    @Test
    void repeatVoteBeforeEndTimeSameRestaurant() {
        assertTrue(voteService.voteToRestaurant(RESTAURANT_1.getId(), USER1.getId(), TODAY_DATE, BEFORE_END_TIME_IS_VOTING));
        Optional<Vote> vote = voteRepository.findByUserAndDate(USER1.getId(), TODAY_DATE);
        assertTrue(vote.isPresent());
        assertMatchRestaurants(vote.get().getRestaurant(), RESTAURANT_1);
    }

    @Test
    void repeatBeforeEndTimeAnotherRestaurant() {
        assertTrue(voteService.voteToRestaurant(RESTAURANT_2.getId(), USER2.getId(), TODAY_DATE, BEFORE_END_TIME_IS_VOTING));
        Optional<Vote> vote = voteRepository.findByUserAndDate(USER2.getId(), TODAY_DATE);
        assertTrue(vote.isPresent());
        assertMatchRestaurants(vote.get().getRestaurant(), RESTAURANT_2);
    }
}