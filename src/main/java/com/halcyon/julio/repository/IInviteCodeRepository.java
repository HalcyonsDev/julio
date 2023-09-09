package com.halcyon.julio.repository;

import com.halcyon.julio.model.InviteCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface IInviteCodeRepository extends JpaRepository<InviteCode, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE InviteCode code SET code.activationsLeft = ?1 WHERE code.id = ?2")
    void updateActivationsLeftById(Integer activationsLeft, Long id);

    Optional<InviteCode> findByValue(String value);
}