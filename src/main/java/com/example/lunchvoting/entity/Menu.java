package com.example.lunchvoting.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "MENUS", uniqueConstraints = {@UniqueConstraint(columnNames = {"date", "restaurant_id"}, name = "date_restaurant_id_idx")})
@Getter
@Setter
@NoArgsConstructor
public class Menu extends BaseEntity {

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "menu")
    @JsonManagedReference
    private List<Dish> dishes;

    public Menu(LocalDate date, Restaurant restaurant) {
        this.date = date;
        this.restaurant = restaurant;
    }

    public Menu(Integer id, LocalDate date, Restaurant restaurant) {
        this(date, restaurant);
        this.id = id;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "date=" + date +
                '}';
    }
}
