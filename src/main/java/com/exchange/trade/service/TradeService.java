package com.exchange.trade.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exchange.trade.AppConfig;
import com.exchange.trade.model.Coin;
import com.exchange.trade.model.Trade;
import com.exchange.trade.model.TradeType;
import com.exchange.trade.model.User;
import com.exchange.trade.model.Wallet;
import com.exchange.trade.repository.CoinRepository;
import com.exchange.trade.repository.TradeRepository;
import com.exchange.trade.repository.UserRepository;
import com.exchange.trade.repository.WalletRepository;

import jakarta.transaction.Transactional;

@Service
public class TradeService {
    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    CoinRepository coinRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WalletService walletService;

    @Autowired
    AppConfig appConfig;

    @Transactional
    public void createTrade(Integer userId, String coinACode, String coinBCode, 
                        BigDecimal coinAValue, BigDecimal coinBValue, 
                        TradeType tradeType, BigDecimal price) throws InterruptedException {


        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) {
            throw new RuntimeException("User not found");
        }

        // Step 1: Find Coin A
        Optional<Coin> coinA = coinRepository.findByCode(coinACode);
        if (!coinA.isPresent()) {
            throw new RuntimeException("Invalid coinA");
        }

        // Step 2: Find Coin B
        Optional<Coin> coinB = coinRepository.findByCode(coinBCode);
        if (!coinB.isPresent()) {
            throw new RuntimeException("Invalid coinB");
        }

        // Step 3: Validate Trade Type
        if (tradeType == null) {
            throw new RuntimeException("Trade type cannot be null");
        }

        int adminUserId = appConfig.getTrade().getAdmin();

        Wallet userWalletA = walletService.getWallet(userId, coinA.get().getId());
        Wallet userWalletB = walletService.getWallet(userId, coinB.get().getId());

        Wallet adminWalletA = walletService.getWallet(adminUserId, coinA.get().getId());
        Wallet adminWalletB = walletService.getWallet(adminUserId, coinB.get().getId());

    
        // Proceed with the trade logic
        if (tradeType == TradeType.BUY) {
            walletService.creditToWallet(userWalletA, coinAValue);
            walletService.debitFromWallet(userWalletB, coinBValue);
            
            walletService.creditToWallet(adminWalletB, coinBValue);
            walletService.debitFromWallet(adminWalletA, coinAValue);
            
            

        } else if (tradeType == TradeType.SELL) {

            walletService.debitFromWallet(userWalletA, coinAValue);
            walletService.creditToWallet(userWalletB, coinBValue);

            walletService.debitFromWallet(adminWalletB, coinBValue);
            walletService.creditToWallet(adminWalletA, coinAValue);
            
        }

        System.out.printf("%s value = %s and %s value = %s\n", coinA.get().getCode(), coinAValue, coinB.get().getCode(), coinBValue);
        // Step 4: Create a new Trade entity and populate its fields
        Trade trade = new Trade();
        trade.setUser(user.get());  // Associate with the user making the trade
        trade.setCoinA(coinA.get()); // Set the Coin A object
        trade.setCoinB(coinB.get()); // Set the Coin B object
        trade.setCoinAValue(coinAValue); // Set the amount of coinA
        trade.setCoinBValue(coinBValue); // Set the amount of coinB
        trade.setType(tradeType.ordinal());  // Set the trade type (BUY/SELL)
        trade.setPrice(price);          // Set the price (if needed for your calculation)
        
        // Step 5: Calculate and validate the trade values (price check)
        if (tradeType == TradeType.BUY) {
            // If it's a buy, you can calculate coinBValue based on the price of coinA
            BigDecimal calculatedCoinBValue = coinAValue.multiply(price);
            calculatedCoinBValue = calculatedCoinBValue.setScale(2, RoundingMode.HALF_UP);
            System.out.printf("Comparing %s and %s", calculatedCoinBValue, coinBValue);
            
            if (coinBValue.compareTo(calculatedCoinBValue) != 0) {
                throw new RuntimeException("Price mismatch: CoinB value is incorrect for this trade.");
            }
        } else if (tradeType == TradeType.SELL) {
            // If it's a sell, you can calculate coinAValue based on the price of coinB
            System.out.printf("Dividng %s value %s by price %s\n", coinB.get().getCode(), coinBValue, price);
            BigDecimal calculatedCoinAValue = coinBValue.divide(price, 8, RoundingMode.HALF_UP);
            calculatedCoinAValue = calculatedCoinAValue.setScale(4, RoundingMode.HALF_UP);
            System.out.printf("Comparing %s and %s", calculatedCoinAValue, coinAValue);
            if (coinAValue.compareTo(calculatedCoinAValue) != 0) {
                throw new RuntimeException("Price mismatch: CoinA value is incorrect for this trade.");
            }
        }

        // Step 6: Set trade status (default to '1' for success or as per your business logic)
        trade.setStatus(1);  // You can adjust this based on your own logic for status (e.g., pending, completed)

        // Step 7: Set created and modified timestamps
        LocalDateTime now = LocalDateTime.now();
        trade.setCreatedAt(now);
        trade.setModifiedAt(now);

        // Step 8: Save the trade to the database
        tradeRepository.save(trade);
    }

    public List<Trade> getAllTrades() {
        return tradeRepository.findAll();
    }
}

