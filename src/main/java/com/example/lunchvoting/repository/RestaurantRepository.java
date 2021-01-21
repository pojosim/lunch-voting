package com.example.lunchvoting.repository;

import com.example.lunchvoting.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Transactional
    @Override
    <T extends Restaurant> T save(T item);

    @Transactional
    @Override
    void deleteById(Integer id);

    Restaurant findRestaurantsByName(String name);

    @Query("select r from Restaurant r join fetch r.menus m where m.date = ?1")
    List<Restaurant> findRestaurantsWithMenuByDate(LocalDate date);

    @Query("select r from Restaurant r join fetch r.menus m where r.id = :restaurantId and m.date = :date")
    Optional<Restaurant> findByIdAndMenuDate(@Param("restaurantId") Integer restaurantId, @Param("date") LocalDate date);

}
