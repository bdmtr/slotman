package com.bdmtr.slotman.model.entity;


import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;

    @NotBlank(message = "username cant be blank")
    @Min(value = 5, message = "username must be 5 symbols minimum")
    @Max(value = 15, message = "username cannot be more than 15 symbols")
    private String username;

    @NotBlank(message = "password cant be blank")
    @Min(value = 5, message = "password must be 5 symbols minimum")
    @Max(value = 15, message = "password cannot be more than 15 symbols")
    private String password;
    private int income;
    private int outcome;
    private Timestamp creationDate;

    @ManyToOne
    @JoinColumn(name = "roleId")
    Role role;

    @ManyToOne
    @JoinColumn(name = "statusId")
    Status status;

    public User() {
    }

    public User(int id, String username, String password, int income, int outcome, Timestamp creationDate, Role role, Status status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.income = income;
        this.outcome = outcome;
        this.creationDate = creationDate;
        this.role = role;
        this.status = status;
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
