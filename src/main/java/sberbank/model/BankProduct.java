package sberbank.model;

import lombok.Getter;
import sberbank.common.ErrorMessages;


public abstract class BankProduct {
    protected String name;
    protected String currency;
    @Getter
    protected double balance;

    public BankProduct(String name, String currency, double balance) {
        this.name = name;
        this.currency = currency;
        validBalance(balance);
        this.balance = balance;
    }

    protected void validBalance(double balance) {
        if (balance < 0) throw new IllegalArgumentException(ErrorMessages.NEGATIVE_BALANCE);
    }

    protected void validatePositive(double amount) {
        if (amount <= 0) throw new IllegalArgumentException(ErrorMessages.NEGATIVE_AMOUNT);
    }

}
