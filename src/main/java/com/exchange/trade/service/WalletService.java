package com.exchange.trade.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.retry.annotation.Backoff;

import com.exchange.trade.model.Wallet;
import com.exchange.trade.repository.WalletRepository;

@Service
public class WalletService {

    @Autowired
    WalletRepository walletRepository;
    
    public void debitFromWallet(Integer userId, Integer coinId, BigDecimal amount) throws InterruptedException {
    
        Wallet wallet = getWallet(userId, coinId);
           
        if (wallet.getBalance().compareTo(amount) <= 0) {
            throw new RuntimeException("Insufficient balance");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        checkInsufficientBalance(wallet);
        
        walletRepository.save(wallet);
    }

    
    public void creditToWallet(Integer userId, Integer coinId, BigDecimal amount) throws InterruptedException {
       
        Wallet wallet = getWallet(userId, coinId);
               
        wallet.setBalance(wallet.getBalance().add(amount));
        checkInsufficientBalance(wallet);
       
        walletRepository.save(wallet);
              
    }

    private void checkInsufficientBalance(Wallet wallet) {
        if(wallet.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Balance insufficient");
        }
    }

    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public Wallet getWallet(int userId, int coinId) {
        System.out.printf("Trying for user %s and coin %s\n", userId, coinId);
        return walletRepository.findAndLockByUserIdAndCoinId(userId, coinId)
        .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    public void creditToWallet(Wallet wallet, BigDecimal amount) {
        wallet.setBalance(wallet.getBalance().add(amount));
        checkInsufficientBalance(wallet);
        walletRepository.save(wallet);
    }

    public void debitFromWallet(Wallet wallet, BigDecimal amount){
        if (wallet.getBalance().compareTo(amount) <= 0) {
            throw new RuntimeException("Insufficient balance");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        checkInsufficientBalance(wallet);
        
        walletRepository.save(wallet);
    }
}
