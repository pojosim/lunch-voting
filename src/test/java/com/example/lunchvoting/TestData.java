package com.example.lunchvoting;

import com.example.lunchvoting.entity.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestData {

    public static final LocalDate expectedDate = LocalDate.now();
    public static final LocalDate otherDate1 = LocalDate.of(2021, Month.JANUARY, 8);
    public static final LocalDate otherDate2 = LocalDate.of(2021, Month.JANUARY, 9);

    public static final Restaurant RESTAURANT_1 = new Restaurant(1000, "Lerua-Merlo");
    static {
        Menu menu1 = new Menu(1000, expectedDate, RESTAURANT_1);
        menu1.setDishes(List.of(
                new Dish(1000, "dish1", new BigDecimal("10.00"), menu1),
                new Dish(1001, "dish2", new BigDecimal("60.00"), menu1),
                new Dish(1002, "dish3", new BigDecimal("88.00"), menu1)
        ));

        Menu menu2 = new Menu(1001, otherDate1, RESTAURANT_1);
        menu2.setDishes(List.of(
                new Dish(1003, "dish4", new BigDecimal("55.00"), menu2),
                new Dish(1004, "dish5", new BigDecimal("80.00"), menu2)
        ));

        RESTAURANT_1.setMenus(List.of(menu1, menu2));
    }

    public static final Restaurant RESTAURANT_2 = new Restaurant(1001, "Potato Club");
    static {
        Menu menu1 = new Menu(1002, expectedDate, RESTAURANT_2);
        menu1.setDishes(List.of(
                new Dish(1005, "dish1", new BigDecimal("78.00"), menu1),
                new Dish(1006, "dish2", new BigDecimal("44.00"), menu1),
                new Dish(1007, "dish3", new BigDecimal("68.00"), menu1)
        ));

        Menu menu2 = new Menu(1003, otherDate2, RESTAURANT_2);
        menu2.setDishes(List.of(
                new Dish(1008, "dish1", new BigDecimal("49.00"), menu2),
                new Dish(1009, "dish2", new BigDecimal("12.00"), menu2),
                new Dish(1010, "dish3", new BigDecimal("98.00"), menu2)
        ));

        RESTAURANT_2.setMenus(List.of(menu1, menu2));
    }

    public static final Restaurant RESTAURANT_3 = new Restaurant(1002,"Shaurma");
    static {
        Menu menu = new Menu(1004, otherDate1, RESTAURANT_3);
        menu.setDishes(List.of(
                new Dish(1011, "dish1", new BigDecimal("180.00"), menu),
                new Dish(1012, "dish2", new BigDecimal("99.00"), menu)));

        RESTAURANT_3.setMenus(List.of(menu));
    }
    public static final List<Restaurant> allSaveRestaurants = List.of(RESTAURANT_1, RESTAURANT_2, RESTAURANT_3);

    public static final Restaurant RESTAURANT_4_NON_SAVE = new Restaurant("Sun-city");
    static {
        Menu menu = new Menu(otherDate1, RESTAURANT_4_NON_SAVE);
        menu.setDishes(List.of(
                new Dish("dish1", new BigDecimal("74.00"), menu),
                new Dish("dish2", new BigDecimal("250.00"), menu)));

        RESTAURANT_4_NON_SAVE.setMenus(List.of(menu));
    }

    public static final Restaurant RESTAURANT_5_NON_SAVE = new Restaurant("Hello restaurant");
    static {
        Menu menu = new Menu(otherDate1, RESTAURANT_5_NON_SAVE);
        menu.setDishes(List.of(
                new Dish("dish1", new BigDecimal("74.00"), menu),
                new Dish("dish2", new BigDecimal("250.00"), menu)));

        RESTAURANT_5_NON_SAVE.setMenus(List.of(menu));
    }

    public static final User USER1 = new User();
    static {
        USER1.setId(1000);
        USER1.setName("User1");
        USER1.setPassword("12345678");
        USER1.setEmail("user1@google.com");
        USER1.setRoles(Set.of(Role.USER));
    }
    public static final User USER2 = new User();
    static {
        USER2.setId(1001);
        USER2.setName("User2");
        USER2.setPassword("87654321");
        USER2.setEmail("user2@google.com");
        USER2.setRoles(Set.of(Role.USER));
    }
    public static final User USER3 = new User();
    static {
        USER3.setId(1002);
        USER3.setName("User3");
        USER3.setPassword("27654322");
        USER3.setEmail("user3@google.com");
        USER3.setRoles(Set.of(Role.USER));
    }
    public static final User USER4 = new User();
    static {
        USER4.setId(1003);
        USER4.setName("User4");
        USER4.setPassword("2777894322");
        USER4.setEmail("user4@google.com");
        USER4.setRoles(Set.of(Role.USER));
    }
    public static final User ADMIN = new User();
    static {
        ADMIN.setId(1004);
        ADMIN.setName("Admin");
        ADMIN.setPassword("admin2899999");
        ADMIN.setEmail("admin@google.com");
        ADMIN.setRoles(Set.of(Role.ADMIN, Role.USER));
    }
    public static final List<User> allUsers = List.of(USER1, USER2, USER3, USER4, ADMIN);

    public static final Vote VOTE1 = new Vote(expectedDate, USER1, RESTAURANT_1);
    public static final Vote VOTE2 = new Vote(expectedDate, USER2, RESTAURANT_1);
    public static final Vote VOTE3 = new Vote(expectedDate, USER3, RESTAURANT_2);
    public static final List<Vote> allVotes = List.of(VOTE1, VOTE2, VOTE3);
    public static final Map<Integer, Integer> mapRestaurantIdToCountVote = new HashMap<>() {{
        put(RESTAURANT_1.getId(), 2);
        put(RESTAURANT_2.getId(), 1);
        put(RESTAURANT_3.getId(), 0);
    }};

    public static Menu getTestingMenu(LocalDate date, Restaurant restaurant) {
        Menu menu = new Menu(date, restaurant);
        menu.setDishes(List.of(
                new Dish("Caesar salad", new BigDecimal("123"), menu),
                new Dish("Trout caviar", new BigDecimal("321"), menu)
        ));
        return menu;
    }

}
