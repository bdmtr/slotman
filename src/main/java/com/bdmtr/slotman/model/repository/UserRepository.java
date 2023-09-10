package com.bdmtr.slotman.model.repository;

import com.bdmtr.slotman.model.entity.Status;
import com.bdmtr.slotman.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Modifying
    @Query("UPDATE User u SET u.income = :income WHERE u.id = :userId")
    void updateIncomeById(@Param("userId") Integer id, @Param("income") int income);

    @Modifying
    @Query("UPDATE User u SET u.outcome = :outcome WHERE u.id = :userId")
    void updateOutcomeById(@Param("userId") Integer id, @Param("outcome") int outcome);

    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.id = :userId")
    int updateUserStatusById(Integer userId, Status status);

    Optional<User> findByUsername(String username);
}
