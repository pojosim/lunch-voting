package com.example.lunchvoting.repository;

import com.example.lunchvoting.AbstactTest;
import com.example.lunchvoting.entity.Vote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.example.lunchvoting.TestData.*;

class VoteRepositoryTest extends AbstactTest {

    @Test
    void deleteByUser() {
        voteRepository.deleteByUser(USER3);
        Assertions.assertFalse(voteRepository.existsById(VOTE3.getId()));
    }

    @Test
    void deleteByUserAndDate() {
        voteRepository.deleteByUserAndDate(USER3, expectedDate);
        Assertions.assertFalse(voteRepository.existsById(VOTE3.getId()));
    }

    @Test
    void getCountVotesByDate() {
        int expectedCountVote = voteRepository.getCountVotesByDate(RESTAURANT_1.getId(), expectedDate);
        Assertions.assertEquals(expectedCountVote, mapRestaurantIdToCountVote.get(RESTAURANT_1.getId()));
    }

    @Test
    void findByUserAndDate() {
        Vote expectedVote = voteRepository.findByUserAndDate(USER2.getId(), expectedDate).orElse(null);
        Assertions.assertNotNull(expectedVote);
        Assertions.assertEquals(expectedVote, VOTE2);

        Vote expectedNullVote = voteRepository.findByUserAndDate(USER4.getId(), expectedDate).orElse(null);
        Assertions.assertNull(expectedNullVote);
    }
}