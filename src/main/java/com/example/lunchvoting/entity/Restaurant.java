package com.example.lunchvoting.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "RESTAURANTS", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "name_idx")})
@Getter
@Setter
@NoArgsConstructor
public class Restaurant extends BaseEntity {

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "name", nullable = false)
    protected String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "restaurant", orphanRemoval = true)
    @OrderBy("date")
    @JsonManagedReference
    private List<Menu> menus;

    public Restaurant(@NotBlank @Size(min = 2, max = 100) String name) {
        this.name = name;
    }

    public Restaurant(Integer id, String name) {
        this(name);
        this.id = id;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                '}';
    }
}
