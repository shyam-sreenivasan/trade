package com.exchange.trade.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "trade", schema = "exchange")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "coin_a", nullable = false)
    private Coin coinA;  // Reference to Coin A (e.g., BTC)

    @ManyToOne
    @JoinColumn(name = "coin_b", nullable = false)
    private Coin coinB;  // Reference to Coin B (e.g., INR)

    @Column(name = "coina_value", columnDefinition = "NUMERIC(40,8)")
    private BigDecimal coinAValue;  // Amount of coinA (e.g., 0.5 BTC)

    @Column(name = "coinb_value", columnDefinition = "NUMERIC(40,8)")
    private BigDecimal coinBValue;  // Amount of coinB (e.g., 40,000 INR)

    @Column(name = "trade_type")
    private int type;  // BUY 0 or SELL 1

    private BigDecimal price;  // The price at which the trade happened (e.g., 1 BTC = 80,000 INR)

    private int status;  // success or failed

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
}
