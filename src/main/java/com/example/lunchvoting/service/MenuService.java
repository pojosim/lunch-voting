package com.example.lunchvoting.service;

import com.example.lunchvoting.entity.Dish;
import com.example.lunchvoting.entity.Menu;
import com.example.lunchvoting.repository.MenuRepository;
import com.example.lunchvoting.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.lunchvoting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MenuService implements com.example.lunchvoting.service.Service {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuService(MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Menu saveOrUpdate(Integer restaurantId, List<Dish> dishes, LocalDate date) {
        checkNotFoundWithId(restaurantRepository.existsById(restaurantId), restaurantId);

        Optional<Menu> optionalMenu = menuRepository.findMenuByRestaurantAndDate(restaurantId, date);

        if (optionalMenu.isPresent()) {
            Menu menu = optionalMenu.get();
            menu.getDishes().clear();
            menu.getDishes().addAll(dishes);
            dishes.forEach(dish -> dish.setMenu(menu));
            return menuRepository.save(menu);
        } else {
            Menu menu = new Menu(date, restaurantRepository.getOne(restaurantId), dishes);
            dishes.forEach(dish -> dish.setMenu(menu));
            return menuRepository.save(menu);
        }
    }

    public void delete(Integer restaurantId, LocalDate date) {
        checkNotFoundWithId(restaurantRepository.existsById(restaurantId), restaurantId);
        menuRepository.deleteByRestaurantIdAndDate(restaurantId, date);
    }

    @Override
    public boolean checkIdExists(Integer id) {
        return menuRepository.existsById(id);
    }

    public boolean checkDateIsExists(LocalDate date, Integer restaurantId) {
        return menuRepository.findMenuByRestaurantAndDate(restaurantId, date).isPresent();
    }
}
