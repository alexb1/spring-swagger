package com.abel.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * User entity class.
 *
 * @author Alex Belikov
 */
@Entity
@Getter
@Setter
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "ACCOUNT_NAME")
    @NotNull(message = "error.name.notnull")
    @Size(min = 1, max = 100, message = "error.name.size")
    @JsonProperty(required = true)
    @ApiModelProperty(notes = "User's name", required = true)
    private String name;

    @Column(name = "PASSWORD")
    @NotNull(message = "error.password.notnull")
    @Size(min = 6, max = 20, message = "error.password.size")
    @JsonProperty(required = true)
    @ApiModelProperty(notes = "User's password", required = true)
    private String password;

    @Column(name = "EMAIL")
    @Email(message = "error.email.format")
    @NotNull
    @JsonProperty(required = true)
    @ApiModelProperty(name = "User's email", required = true)
    private String email;

    public User() {
    }

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
}
