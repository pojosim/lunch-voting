package com.example.lunchvoting.entity;

import com.example.lunchvoting.web.dto.DishTo;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "DISHES", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "price", "menu_id"}, name = "name_price_menu_idx")})
@Getter
@Setter
@NoArgsConstructor
public class Dish extends BaseEntity {

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "name", nullable = false)
    protected String name;

    @NotNull
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    Menu menu;

    public Dish(String name, BigDecimal price, Menu menu) {
        this.name = name;
        this.price = price;
        this.menu = menu;
    }

    public Dish(Integer id, String name, BigDecimal price, Menu menu) {
        this(name, price, menu);
        this.id = id;
    }

    public Dish(DishTo dishTo, Menu menu) {
        name = dishTo.getName();
        price = dishTo.getPrice();
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
