package com.bdmtr.slotman.model.entity;


import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * The User class represents the users.
 * Role can be "USER" or "ADMIN".
 * Status can be "ACTIVE" or "BLOCKED".
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    @Schema(name = "User ID", example = "1")
    private int id;

    @NotBlank(message = "username cant be blank")
    @Size(min=5, max=15, message = "username must be 5 symbols minimum, 15 maximum")
    @Schema(name = "User name/login", example = "userman")
    private String username;

    @NotBlank(message = "password cant be blank")
    @Size(min=5)
    @Schema(name = "User password", example = "pass123")
    private String password;
    private int income;
    private int outcome;
    private Timestamp creationDate;

    @ManyToOne
    @JoinColumn(name = "roleId")
    @Schema(name = "Role of current user. Can be ADMIN or USER", example = "USER")
    Role role;

    @ManyToOne
    @JoinColumn(name = "statusId")
    @Schema(name = "User status", example = "active")
    Status status;

    public User(String username, String password, int income, int outcome, Timestamp creationDate, Role role, Status status) {
        this.username = username;
        this.password = password;
        this.income = income;
        this.outcome = outcome;
        this.creationDate = creationDate;
        this.role = role;
        this.status = status;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public int getOutcome() {
        return outcome;
    }

    public void setOutcome(int outcome) {
        this.outcome = outcome;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && income == user.income && outcome == user.outcome && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(creationDate, user.creationDate) && Objects.equals(role, user.role) && Objects.equals(status, user.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, income, outcome, creationDate, role, status);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", income=" + income +
                ", outcome=" + outcome +
                ", creationDate=" + creationDate +
                ", role=" + role +
                ", status=" + status +
                '}';
    }
}
