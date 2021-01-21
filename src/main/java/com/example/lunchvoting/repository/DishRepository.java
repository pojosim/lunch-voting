package com.example.lunchvoting.repository;

import com.example.lunchvoting.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface DishRepository extends JpaRepository<Dish, Integer> {

    @Transactional
    @Override
    <T extends Dish> T save(T item);

    @Transactional
    @Override
    void deleteById(Integer id);

    @Transactional
    @Modifying
    @Query("delete from Dish where menu.id = ?1")
    void deleteAllDishesByMenuId(Integer menuId);

}
