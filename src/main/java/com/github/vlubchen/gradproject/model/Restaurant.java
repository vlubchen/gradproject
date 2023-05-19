package com.github.vlubchen.gradproject.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.vlubchen.gradproject.util.validation.NoHtml;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;

@Entity
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"},
        name = "restaurant_unique_name")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {

    @Column(name = "phone")
    @NotBlank
    @Size(max = 25)
    @NoHtml
    private String phone;

    @Column(name = "address")
    @NotBlank
    @Size(max = 255)
    @NoHtml
    private String address;

    @Column(name = "email")
    @Email
    @NotBlank
    @Size(max = 128)
    @NoHtml
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Set<LunchItem> lunchItems;

    public Restaurant(Integer id, String name, String phone, String address, String email) {
        super(id, name);
        this.phone = phone;
        this.address = address;
        this.email = email;
    }

    public Restaurant(Restaurant restaurant) {
        this(restaurant.id, restaurant.name, restaurant.phone, restaurant.address, restaurant.email);
    }
}
