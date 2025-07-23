package sberbank.product;

import sberbank.common.ErrorMessages;
import sberbank.model.BankProduct;
import sberbank.model.Depositable;

public class Deposit extends BankProduct implements Depositable {
    private boolean isClosed = false;

    public Deposit(String name, String currency, double balance) {
        super(name, currency, balance);
    }

    @Override
    public void deposit(double amount) {
        if (isClosed) throw new IllegalStateException(ErrorMessages.DEPOSIT_CLOSED);
        super.validatePositive(amount);
        balance += amount;
    }

    public void close() {
        isClosed = true;
    }
}
