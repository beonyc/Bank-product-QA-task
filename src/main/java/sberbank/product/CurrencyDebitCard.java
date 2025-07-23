package sberbank.product;

import sberbank.model.BankProduct;

public class CurrencyDebitCard extends DebitCard {
    public CurrencyDebitCard(String name, String currency, double balance) {
        super(name, currency, balance);
    }
}
