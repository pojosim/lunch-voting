package com.example.lunchvoting.repository;

import com.example.lunchvoting.AbstractTest;
import com.example.lunchvoting.TestData;
import com.example.lunchvoting.entity.Vote;
import org.junit.jupiter.api.Test;

import static com.example.lunchvoting.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

class VoteRepositoryTest extends AbstractTest {

    @Test
    void save() {
        Vote expectedVote = TestData.createNewVote(TODAY_DATE, USER4, RESTAURANT_1);
        voteRepository.save(expectedVote);
        Vote actualVote = voteRepository.findById(expectedVote.getId()).orElse(null);
        assertNotNull(actualVote);
        assertMatchVote(expectedVote, actualVote);

        Vote actualVoteByUserAndDate = voteRepository.findByUserAndDate(USER4.getId(), TODAY_DATE).orElse(null);
        assertNotNull(actualVoteByUserAndDate);
        assertEquals(expectedVote, actualVoteByUserAndDate);
    }

    @Test
    void deleteByUser() {
        voteRepository.deleteByUser(USER3);
        assertFalse(voteRepository.existsById(VOTE3.getId()));
    }

    @Test
    void deleteByUserAndDate() {
        voteRepository.deleteByUserAndDate(USER3, TODAY_DATE);
        assertFalse(voteRepository.existsById(VOTE3.getId()));
    }

    @Test
    void getCountVotesByDate() {
        int actualCountVote = voteRepository.getCountVotesByDate(RESTAURANT_1.getId(), TODAY_DATE);
        assertEquals(mapRestaurantIdToCountVote.get(RESTAURANT_1.getId()), actualCountVote);
    }

    @Test
    void findByUserAndDate() {
        Vote actualVote = voteRepository.findByUserAndDate(USER2.getId(), TODAY_DATE).orElse(null);
        assertNotNull(actualVote);
        assertMatchVote(VOTE2, actualVote);

        Vote actualNullVote = voteRepository.findByUserAndDate(USER4.getId(), TODAY_DATE).orElse(null);
        assertNull(actualNullVote);
    }
}