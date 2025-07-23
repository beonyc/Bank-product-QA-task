package sberbank.product;

import sberbank.common.ErrorMessages;
import sberbank.model.Card;

public class CreditCard extends Card {
    private final double interestRate;

    private double debt = 0.0;
    //Процентный долг
    private double interestDebt = 0.0;
    private double primalBalance = 0.0;

    public CreditCard(String name, String currency, double balance, double interestRate) {
        super(name, currency, balance);
        validateInterestRate(interestRate);
        this.interestRate = interestRate / 100;
        primalBalance = balance;
    }

    public double getDebt() {
        return debt + interestDebt;
    }

    private double calculateInterestDebt(double amount) {
        return amount * interestRate;
    }

    @Override
    public void withdraw(double amount) {
        validatePositive(amount);

        // Если баланс выше начального (пополнена сверх лимита)
        if (balance > primalBalance) {
            double availableWithoutDebt = balance - primalBalance;

            if (amount <= availableWithoutDebt) {
                // Просто уменьшаем баланс — долга нет
                balance -= amount;
            } else {
                // Часть идёт в долг
                double debtPart = amount - availableWithoutDebt;
                balance -= amount;
                debt += debtPart;
                interestDebt += calculateInterestDebt(debtPart);
            }

        } else {
            // Баланс не превышает начальный — всё идёт в долг
            balance -= amount;
            debt += amount;
            interestDebt += calculateInterestDebt(amount);
        }

    }

    @Override
    public void deposit(double amount) {
        validatePositive(amount);
        if (interestDebt == 0) {
            balance += amount;
            debt = balance >= primalBalance ? 0 : debt - amount;
        } else {
            if (amount > interestDebt) {
                amount -= interestDebt;
                interestDebt = 0;
                balance += amount;
                debt = balance >= primalBalance ? 0 : debt - amount;
            } else {
                interestDebt -= amount;
            }
        }
    }

    protected void validateInterestRate(double interestRate) {
        if (interestRate < 0) throw new IllegalArgumentException(ErrorMessages.NEGATIVE_RATE);
    }


}
