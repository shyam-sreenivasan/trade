package com.exchange.trade.dto;


import com.exchange.trade.model.TradeType;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TradeDto {
    Integer userId;

    @NotBlank
    String coinACode;

    @NotBlank
    String coinBCode;

    
    String coinAValue;
    String coinBValue;
    TradeType tradeType;
    String price;
}
