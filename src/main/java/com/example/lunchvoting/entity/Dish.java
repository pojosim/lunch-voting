package com.example.lunchvoting.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name = "price", nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_id")
    @JsonBackReference
    Menu menu;

    public Dish(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Dish(String name, BigDecimal price, Menu menu) {
        this(name, price);
        this.menu = menu;
    }

    public Dish(Integer id, String name, BigDecimal price, Menu menu) {
        this(name, price, menu);
        this.id = id;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
