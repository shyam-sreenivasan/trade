package com.exchange.trade.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exchange.trade.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    
}
