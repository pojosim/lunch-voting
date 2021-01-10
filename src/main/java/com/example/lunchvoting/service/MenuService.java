package com.example.lunchvoting.service;

import com.example.lunchvoting.entity.Menu;
import com.example.lunchvoting.entity.Restaurant;
import com.example.lunchvoting.repository.MenuRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class MenuService implements com.example.lunchvoting.service.Service {
    private final MenuRepository menuRepository;

    @PersistenceContext
    EntityManager em;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Menu createMenu(Integer restaurantId, LocalDate date) {
        Menu menu = new Menu();
        menu.setRestaurant(em.getReference(Restaurant.class, restaurantId));
        menu.setDate(date);
        return menuRepository.save(menu);
    }

    @Override
    public boolean checkIdExists(Integer id) {
        return menuRepository.existsById(id);
    }

    public boolean checkDateIsExists(LocalDate date, Integer restaurantId) {
        return Optional.ofNullable(menuRepository.findMenuByRestaurantAndDate(restaurantId, date)).isPresent();
    }
}
