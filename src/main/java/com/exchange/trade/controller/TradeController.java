package com.exchange.trade.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.exchange.trade.dto.TradeDto;
import com.exchange.trade.model.Trade;
import com.exchange.trade.model.TradeType;
import com.exchange.trade.service.TradeService;

@RestController
public class TradeController {

    @Autowired
    TradeService tradeService;

    @GetMapping("/trades")
    public ResponseEntity<List<Trade>> getTrades() {
        return ResponseEntity.ok(tradeService.getAllTrades());
    }

    @PostMapping("/trades")
    public ResponseEntity<String> createTrade(@RequestBody TradeDto tradeDto) {
        try {
            Integer userId = tradeDto.getUserId();
            String coinA = tradeDto.getCoinACode();
            String coinB = tradeDto.getCoinBCode();
            BigDecimal coinAValue = new BigDecimal(tradeDto.getCoinAValue());
            BigDecimal coinBValue = new BigDecimal(tradeDto.getCoinBValue());
            BigDecimal price = new BigDecimal(tradeDto.getPrice());
            TradeType tradetype = tradeDto.getTradeType();
            tradeService.createTrade(userId, coinA, coinB, coinAValue, coinBValue, tradetype, price);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
        return ResponseEntity.ok("success");
    }
}
