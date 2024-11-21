package com.exchange.trade.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exchange.trade.model.Trade;

public interface TradeRepository extends JpaRepository<Trade, Integer>{
    
}
