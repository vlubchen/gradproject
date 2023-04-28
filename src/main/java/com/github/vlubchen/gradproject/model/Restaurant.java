package com.github.vlubchen.gradproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {

    @Column(name = "phone")
    @NotBlank
    @Size(max = 25)
    private String phone;

    @Column(name = "address")
    @NotBlank
    @Size(max = 255)
    private String address;

    @Column(name = "email")
    @Email
    @NotBlank
    @Size(max = 128)
    private String email;

    public Restaurant(Integer id, String name, String phone, String address, String email) {
        super(id, name);
        this.phone = phone;
        this.address = address;
        this.email = email;
    }
}