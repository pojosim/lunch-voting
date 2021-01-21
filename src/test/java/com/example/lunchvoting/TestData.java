package com.example.lunchvoting;

import com.example.lunchvoting.entity.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class TestData {

    public static final LocalDate TODAY_DATE = LocalDate.now();
    public static final LocalDate BEFORE_DATE = LocalDate.of(2021, Month.JANUARY, 1);

    public static final Restaurant RESTAURANT_1 = new Restaurant(1000, "Lerua-Merlo");

    static {
        Menu menu1 = new Menu(1000, TODAY_DATE, RESTAURANT_1);
        List<Dish> dishes1 = List.of(
            new Dish(1000, "Potato", new BigDecimal("10.00"), menu1),
            new Dish(1001, "Сrab salad", new BigDecimal("60.45"), menu1),
            new Dish(1002, "Burger", new BigDecimal("120.00"), menu1)
        );
        menu1.setDishes(dishes1);

        Menu menu2 = new Menu(1001, BEFORE_DATE, RESTAURANT_1);
        menu2.setDishes(List.of(
                new Dish(1003, "Soup kharcho", new BigDecimal("55.65"), menu2),
                new Dish(1004, "Shawarma", new BigDecimal("80.00"), menu2)
        ));
        RESTAURANT_1.setMenus(List.of(menu1, menu2));
    }

    public static final Restaurant RESTAURANT_2 = new Restaurant(1001, "Potato Club");

    static {
        Menu menu1 = new Menu(1002, TODAY_DATE, RESTAURANT_2);
        List<Dish> dishes1 = List.of(
            new Dish(1005, "Borscht", new BigDecimal("89.90"), menu1),
            new Dish(1006, "Burger", new BigDecimal("44.80"), menu1)
        );
        menu1.setDishes(dishes1);

        Menu menu2 = new Menu(1003, BEFORE_DATE, RESTAURANT_2);
        List<Dish> dishes2 = List.of(
            new Dish(1007, "Сrab salad", new BigDecimal("60.45"), menu2),
            new Dish(1008, "Soup kharcho", new BigDecimal("55.65"), menu2),
            new Dish(1009, "Potato", new BigDecimal("10.00"), menu2)
        );
        menu2.setDishes(dishes2);
        RESTAURANT_2.setMenus(List.of(menu1, menu2));
    }

    public static final Restaurant RESTAURANT_3 = new Restaurant(1002, "Banana restaurant");

    public static final List<Restaurant> allSavedRestaurants = List.of(RESTAURANT_1, RESTAURANT_2, RESTAURANT_3);

    public static final User ADMIN = new User();

    static {
        ADMIN.setId(1000);
        ADMIN.setName("Admin");
        ADMIN.setPassword("AdminQwerty1");
        ADMIN.setEmail("admin@yandex.com");
        ADMIN.setRoles(Set.of(Role.ADMIN, Role.USER));
    }

    public static final User USER1 = new User();

    static {
        USER1.setId(1001);
        USER1.setName("User1");
        USER1.setPassword("Qwerty123");
        USER1.setEmail("user1@yandex.ru");
        USER1.setRoles(Set.of(Role.USER));
    }

    public static final User USER2 = new User();

    static {
        USER2.setId(1002);
        USER2.setName("User2");
        USER2.setPassword("Qwerty234");
        USER2.setEmail("user2@yandex.ru");
        USER2.setRoles(Set.of(Role.USER));
    }

    public static final User USER3 = new User();

    static {
        USER3.setId(1003);
        USER3.setName("User3");
        USER3.setPassword("Qwerty345");
        USER3.setEmail("user3@yandex.ru");
        USER3.setRoles(Set.of(Role.USER));
    }

    public static final User USER4 = new User();

    static {
        USER4.setId(1004);
        USER4.setName("User4");
        USER4.setPassword("Qwerty456");
        USER4.setEmail("user4@yandex.ru");
        USER4.setRoles(Set.of(Role.USER));
    }

    public static final User USER5 = new User();

    static {
        USER5.setId(1005);
        USER5.setName("User5");
        USER5.setPassword("Qwerty567");
        USER5.setEmail("user5@yandex.ru");
        USER5.setRoles(Set.of(Role.USER));
    }

    public static final List<User> allUsers = List.of(ADMIN, USER1, USER2, USER3, USER4, USER5);

    public static final Vote VOTE1 = new Vote(TODAY_DATE, USER1, RESTAURANT_1);
    public static final Vote VOTE2 = new Vote(TODAY_DATE, USER2, RESTAURANT_1);
    public static final Vote VOTE3 = new Vote(TODAY_DATE, USER3, RESTAURANT_2);

    public static final List<Vote> allVotes = List.of(VOTE1, VOTE2, VOTE3);

    public static final Map<Integer, Integer> mapRestaurantIdToCountVote = new HashMap<>() {{
        put(RESTAURANT_1.getId(), 2);
        put(RESTAURANT_2.getId(), 1);
    }};

    public static Menu createNewMenu(LocalDate date, Restaurant restaurant) {
        Menu menu = new Menu(date, restaurant);
        menu.setDishes(List.of(
                new Dish("Caesar salad", new BigDecimal("123.89"), menu),
                new Dish("Trout caviar", new BigDecimal("321.00"), menu)
        ));
        return menu;
    }

    public static List<Dish> createNewDishes() {
        return new LinkedList<>() {{
            add(new Dish("Caesar salad", new BigDecimal("123.89")));
            add(new Dish("Trout caviar", new BigDecimal("321.00")));
        }};
    }

    public static Menu getTodayMenu(Restaurant restaurant) {
        return restaurant.getMenus().stream()
                .filter(menu -> menu.getDate().compareTo(TODAY_DATE) == 0)
                .findFirst().orElseThrow(RuntimeException::new);
    }

    public static Restaurant createNewRestaurant(LocalDate date) {
        Restaurant restaurant_new = new Restaurant("Shaurmichka NEW");
        Menu menu = new Menu(date, restaurant_new);
        menu.setDishes(new LinkedList<>() {{
            add(new Dish("Shaurma pekinskaya", new BigDecimal("200.00"), menu));
            add(new Dish("Shaurma burayatsckay", new BigDecimal("199.99"), menu));
        }});
        restaurant_new.setMenus(new LinkedList<>() {{
            add(menu);
        }});
        return restaurant_new;
    }

    public static Vote createNewVote(LocalDate date, User user, Restaurant restaurant) {
        return new Vote(date, user, restaurant);
    }
}
