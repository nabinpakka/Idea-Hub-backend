package com.example.IdeaHub.auth.model;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "applicationUser")
public class ApplicationUser {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy = "uuid2")
    @ApiModelProperty(
            notes = "Uuid of user",
            name = "uuid",
            required = true,
            example= "98fda03-b259-48c5-bf22-9a5cd538d206"
    )
    private String uuid;

    @ApiModelProperty(
            notes = "Username of user",
            name = "username",
            required = true,
            example= "Angry pig"
    )
    @Column(name = "username", columnDefinition = "VARCHAR(128)")
    private String username;

    @ApiModelProperty(
            notes = "Password of user which is configured to be empty",
            name = "password",
            required = true,
            example= ""
    )
    @Column(name = "password")
    private String password;

    @ApiModelProperty(
            notes = "Role of user",
            name = "role",
            required = true,
            example= "AUTHOR"
    )
    @Column(name = "role")
    private String role;

    public ApplicationUser(String username, String password,
                           String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public ApplicationUser() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "ApplicationUser{" +
                "uuid='" + uuid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
