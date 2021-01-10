package com.example.lunchvoting;

import com.example.lunchvoting.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.List;

@SpringBootTest
@Sql(scripts = {"classpath:db/schema.sql"}, config = @SqlConfig(encoding = "UTF-8"))
@ExtendWith(TimingExtension.class)
public abstract class AbstactTest {
    @Autowired
    protected RestaurantRepository restaurantRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected VoteRepository voteRepository;

    @BeforeEach
    void setUp() {
        restaurantRepository.saveAll(TestData.allSaveRestaurants);
        userRepository.saveAll(TestData.allUsers);
        voteRepository.saveAll(TestData.allVotes);
    }

    @AfterEach
    void tearDown() {
        restaurantRepository.deleteAll();
        userRepository.deleteAll();
        voteRepository.deleteAll();
    }

}
