package com.example.lunchvoting.repository;

import com.example.lunchvoting.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Transactional
    @Modifying
    @Override
    <T extends Menu> T save(T item);

    @Transactional
    @Modifying
    @Override
    void deleteById(Integer id);

    @Transactional
    @Modifying
    @Query("delete from Menu where restaurant.id = :restaurantId and date = :date")
    void deleteByRestaurantIdAndDate(@Param("restaurantId") Integer restaurantId, @Param("date") LocalDate date);

    @Query("select m from Menu m where m.restaurant.id = :restaurantId")
    List<Menu> findMenusByRestaurantId(@Param("restaurantId") Integer restaurantId);

    @Query("select m from Menu m where m.restaurant.id = :restaurant_id and m.date = :date")
    Menu findMenuByRestaurantAndDate(@Param("restaurant_id") Integer restaurantId, @Param("date") LocalDate date);
}
