package com.exchange.trade.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exchange.trade.model.Coin;

public interface CoinRepository extends JpaRepository<Coin, Integer>{
    Optional<Coin> findByCode(String coinCode);
}
