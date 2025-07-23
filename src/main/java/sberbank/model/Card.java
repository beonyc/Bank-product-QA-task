package sberbank.model;

import sberbank.common.ErrorMessages;

public abstract class Card extends BankProduct implements Depositable, Withdrawable {

    public Card(String name, String currency, double balance) {
        super(name, currency, balance);
    }

    @Override
    public void withdraw(double amount) {
        validatePositive(amount);
        if (amount > balance) {
            throw new IllegalArgumentException(ErrorMessages.INSUFFICIENT_FUNDS);
        }
        balance -= amount;

    }

    @Override
    public void deposit(double amount) {
        validatePositive(amount);
        balance += amount;
    }
}
