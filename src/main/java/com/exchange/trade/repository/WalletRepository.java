package com.exchange.trade.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.exchange.trade.model.Wallet;

import jakarta.persistence.LockModeType;


public interface WalletRepository extends JpaRepository<Wallet, Integer>{

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.user.id = :userId AND w.coin.id = :coinId")
    Optional<Wallet> findAndLockByUserIdAndCoinId(@Param("userId") Integer userId, @Param("coinId") Integer coinId);

}
