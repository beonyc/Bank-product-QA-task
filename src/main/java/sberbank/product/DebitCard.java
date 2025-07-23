package sberbank.product;

import sberbank.model.BankProduct;
import sberbank.model.Card;

public class DebitCard extends Card {

    public DebitCard(String name, String currency, double balance) {
        super(name, currency, balance);
    }
}
