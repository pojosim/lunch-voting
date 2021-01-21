package com.example.lunchvoting;

import com.example.lunchvoting.entity.Dish;
import com.example.lunchvoting.entity.Menu;
import com.example.lunchvoting.entity.Restaurant;
import com.example.lunchvoting.entity.Vote;
import com.example.lunchvoting.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = {"classpath:db/schema.sql"}, config = @SqlConfig(encoding = "UTF-8"))
@ExtendWith(TimingExtension.class)
public abstract class AbstractTest {
    @Autowired
    protected RestaurantRepository restaurantRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected VoteRepository voteRepository;
    @Autowired
    protected DishRepository dishRepository;
    @Autowired
    protected MenuRepository menuRepository;

    @BeforeEach
    void setUp() {
        restaurantRepository.saveAll(TestData.allSavedRestaurants);
        userRepository.saveAll(TestData.allUsers);
        voteRepository.saveAll(TestData.allVotes);
    }

    @AfterEach
    void tearDown() {
        restaurantRepository.deleteAll();
        userRepository.deleteAll();
        voteRepository.deleteAll();
    }

    public static void assertMatchRestaurants(Restaurant expectedRestaurant, Restaurant actualRestaurant) {
        assertEquals(expectedRestaurant.getId(), actualRestaurant.getId());
        assertEquals(expectedRestaurant.getName(), actualRestaurant.getName());
    }

    public static void assertMatchMenus(List<Menu> expectedMenus, List<Menu> actualMenus) {
        assertEquals(actualMenus.size(), expectedMenus.size());
        org.assertj.core.api.Assertions.assertThat(actualMenus).hasSameElementsAs(expectedMenus);

        for (Menu actualMenu : actualMenus) {
            int index = expectedMenus.indexOf(actualMenu);
            assertTrue(index >= 0);
            Menu expectedMenu = expectedMenus.get(index);
            assertMatchMenu(expectedMenu, actualMenu);
        }
    }

    public static void assertMatchMenu(Menu expectedMenu, Menu actualMenu) {
        assertEquals(expectedMenu.getId(), actualMenu.getId());
        assertEquals(expectedMenu.getDate(), actualMenu.getDate());

        if (expectedMenu.getDishes() != null && actualMenu.getDishes() != null) {
            assertMatchDishes(expectedMenu.getDishes(), actualMenu.getDishes());
        }
    }

    public static void assertMatchDishes(List<Dish> expectedDishes, List<Dish> actualDishes) {
        assertMatchDishes(expectedDishes, actualDishes, null);
    }

    public static void assertMatchDishes(List<Dish> expectedDishes, List<Dish> actualDishes, String... propsIgnore) {
        assertEquals(expectedDishes.size(), actualDishes.size());
        if (propsIgnore == null) {
            actualDishes.forEach(actualDish -> {
                int index = expectedDishes.indexOf(actualDish);
                assertTrue(index >= 0);
                Dish expectedDish = expectedDishes.get(index);
                assertMatchDish(expectedDish, actualDish);
            });
        } else {
            org.assertj.core.api.Assertions.assertThat(actualDishes).usingElementComparatorIgnoringFields(propsIgnore).isEqualTo(expectedDishes);
        }
    }

    public static void assertMatchDish(Dish expectedDish, Dish actualDish) {
        assertEquals(expectedDish.getId(), actualDish.getId());
        assertEquals(expectedDish.getName(), actualDish.getName());
        assertEquals(expectedDish.getPrice(), actualDish.getPrice());
    }

    public Menu findMenuByNowDate(List<Menu> menus) {
        return findMenuByDate(menus, TestData.TODAY_DATE);
    }

    public Menu findMenuByDate(List<Menu> menus, LocalDate date) {
        return menus.stream().filter(menu -> menu.getDate().isEqual(date)).findFirst().orElseThrow(RuntimeException::new);
    }

    public static void assertMatchVote(Vote expectedVote, Vote actualVote) {
        assertEquals(expectedVote.getId(), actualVote.getId());
        assertEquals(expectedVote.getDate(), actualVote.getDate());
    }

}
