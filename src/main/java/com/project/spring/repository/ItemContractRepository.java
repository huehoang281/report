package com.project.spring.repository;

import com.project.spring.entity.ItemContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemContractRepository extends JpaRepository<ItemContract, Long> {
}
