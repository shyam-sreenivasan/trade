package com.exchange.trade.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "wallet", schema = "exchange")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)  // LAZY fetch for the associated coin
    @JoinColumn(name = "coin_id", referencedColumnName = "id", nullable = false)
    private Coin coin;

    int status;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;

    @Column(name = "balance", columnDefinition = "NUMERIC(40, 8)")
    private BigDecimal balance;
}
