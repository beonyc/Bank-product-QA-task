package sberbank.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sberbank.common.ErrorMessages;
import sberbank.common.TestConstants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Тесты дебетовой карты")
public class DebitCardTest {
    private DebitCard debitCard;

    @BeforeEach
    void setUp() {
        debitCard = new DebitCard(TestConstants.DEBIT_CARD_NAME, TestConstants.CURRENCY_RUB, 1000);
    }


    @Test
    @DisplayName("Снятие положительной суммы денег")
    void withdrawDecreasesBalance() {
        debitCard.withdraw(400);
        assertEquals(600, debitCard.getBalance());
    }


    @Test
    @DisplayName("Снятие положительной суммы денег, которая превышает текущий баланс")
    void withdrawThrowsIfInsufficientFunds() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> debitCard.withdraw(1500));
        assertEquals(ErrorMessages.INSUFFICIENT_FUNDS, e.getMessage());
    }

    @Test
    @DisplayName("Снятие отрицательной суммы денег")
    void withdrawThrowsIfNegative() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> debitCard.withdraw(-500));
        assertEquals(ErrorMessages.NEGATIVE_AMOUNT, e.getMessage());
    }

    @Test
    @DisplayName("Пополнение баланса положительной суммой")
    void depositIncreasesBalance() {
        debitCard.deposit(500);
        assertEquals(1500, debitCard.getBalance());
    }

    @Test
    @DisplayName("Пополнение отрицательной суммы денег")
    void depositThrowsIfNegative() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> debitCard.deposit(-100));
        assertEquals(ErrorMessages.NEGATIVE_AMOUNT, e.getMessage());
    }

    @Test
    @DisplayName("Нельзя создать карту с отрицательным начальным балансом")
    void shouldThrowExceptionWhenCreatingCardWithNegativeInitialBalance() {
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> new DebitCard(TestConstants.DEBIT_CARD_NAME,
                        TestConstants.CURRENCY_RUB,
                        -1));
        assertEquals(ErrorMessages.NEGATIVE_BALANCE, e.getMessage());
    }
}
