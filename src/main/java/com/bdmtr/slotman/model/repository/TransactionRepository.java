package com.bdmtr.slotman.model.repository;


import com.bdmtr.slotman.model.entity.Transaction;

import com.bdmtr.slotman.model.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Transaction findById(Long id);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.type = :type AND t.timestamp BETWEEN :start AND :end")
    List<Transaction> findByUserIdAndTypeAndTimestampBetween(
            @Param("userId") Integer userId,
            @Param("type") TransactionType type,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId")
    Page<Transaction> findAllByUserId(int userId, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.user.username = :username")
    Page<Transaction> findAllByUserName(String username, Pageable pageable);

    List<Transaction> findByUserIdAndType(int userId, TransactionType type);
}
