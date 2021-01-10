package com.example.lunchvoting.web.controller.rest;

import com.example.lunchvoting.entity.Restaurant;
import com.example.lunchvoting.security.AuthorizedUser;
import com.example.lunchvoting.service.RestaurantService;
import com.example.lunchvoting.service.VoteService;
import com.example.lunchvoting.util.ValidationUtil;
import com.example.lunchvoting.util.exception.NotFoundException;
import com.example.lunchvoting.util.exception.VoteRepeatException;
import com.example.lunchvoting.web.dto.RestaurantTo;
import com.example.lunchvoting.web.dto.ResultVote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);

    public static final String REST_URL = Controller.REST_URL + "/restaurant";
    public static final String REST_URL_ADMIN = Controller.REST_URL_ADMIN + "/restaurant";

    private final RestaurantService restaurantService;
    private final VoteService voteService;

    public RestaurantController(RestaurantService restaurantService, VoteService voteService) {
        this.restaurantService = restaurantService;
        this.voteService = voteService;
    }

    @GetMapping(REST_URL)
    public List<RestaurantTo> getTodayRestaurantsWithMenu() {
        LocalDate today = LocalDate.now();
        return restaurantService.getRestaurantsWithMenuByDate(today).stream()
                .map(restaurant -> new RestaurantTo(restaurant, voteService.getCountVotesByDate(restaurant.getId(), today)))
                .collect(Collectors.toList());
    }

    @GetMapping(REST_URL_ADMIN)
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping(REST_URL + "/vote-result")
    public List<ResultVote> getVoteResults() {
        LocalDate today = LocalDate.now();
        return restaurantService.getRestaurantsByDate(today).stream()
                .map(restaurant -> new ResultVote(restaurant, voteService.getCountVotesByDate(restaurant.getId(), today)))
                .collect(Collectors.toList());
    }

    @PostMapping(value = REST_URL + "/{id}/vote")
    public ResponseEntity<List<ResultVote>> takeVoteForRestaurant(@PathVariable("id") Integer restaurantId, @AuthenticationPrincipal AuthorizedUser authUser) {
        LocalDate nowDate = LocalDate.now();
        if (!restaurantService.checkIdExistsAndHasMenuByDate(restaurantId, nowDate)) {
            throw new NotFoundException("not found restaurant with menu");
        }

        if (restaurantService.voteToRestaurant(restaurantId, authUser.getId(), nowDate)) {
            log.debug("create vote user {}, to restaurant {}, to date {}", authUser.getId(), restaurantId, nowDate);
            return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(REST_URL + "/vote-result").build().toUri()).body(getVoteResults());
        } else {
            log.debug("create vote is forbidden, user {}, restaurant {}, date {}", authUser.getId(), restaurantId, nowDate);
            throw new VoteRepeatException("repeat vote is forbidden");
        }
    }

    @PostMapping(value = REST_URL_ADMIN, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createRestaurant(@Valid @RequestBody Restaurant restaurant) {
        ValidationUtil.checkNew(restaurant);
        log.info("create {}", restaurant);
        Restaurant saveRestaurant = restaurantService.save(restaurant);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(saveRestaurant.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(saveRestaurant);
    }

    @DeleteMapping(REST_URL_ADMIN + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRestaurant(@PathVariable("id") Integer restaurantId) {
        log.info("delete {}", restaurantId);
        ValidationUtil.checkNotFoundWithId(restaurantService.checkIdExists(restaurantId), restaurantId);
        restaurantService.deleteRestaurantById(restaurantId);
    }

}
