package com.github.vlubchen.gradproject.to;

import com.github.vlubchen.gradproject.util.validation.NoHtml;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RestaurantTo extends NamedTo {

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

    public RestaurantTo(Integer id, String name, String phone, String address, String email) {
        super(id, name);
        this.phone = phone;
        this.address = address;
        this.email = email;
    }
}
