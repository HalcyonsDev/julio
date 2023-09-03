package com.halcyon.julio.repository;

import com.halcyon.julio.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByValue(String value);
}