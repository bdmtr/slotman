package com.bdmtr.slotman.model.repository;

import com.bdmtr.slotman.model.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    Status findByName(String name);
}
