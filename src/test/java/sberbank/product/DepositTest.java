package sberbank.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sberbank.common.ErrorMessages;
import sberbank.common.TestConstants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Тесты вклада")

public class DepositTest {
    private Deposit deposit;

    @BeforeEach
    void setUp() {
        deposit = new Deposit(TestConstants.DEPOSIT_NAME, TestConstants.CURRENCY_RUB, 1000);
    }

    @Test
    @DisplayName("Пополнение вклада на положительную сумму увеличивает баланс")
    void depositIncreasesBalance() {
        deposit.deposit(500);
        assertEquals(1500, deposit.getBalance());
    }

    @Test
    @DisplayName("Пополнение вклада на отрицательное значение вызывает ошибку")
    void depositNegativeAmountThrowsException() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> deposit.deposit(-100));
        assertEquals(ErrorMessages.NEGATIVE_AMOUNT, e.getMessage());
    }

    @Test
    @DisplayName("После закрытия вклад становится недоступен для пополнения")
    void depositAfterClosingThrowsException() {
        deposit.close();
        Exception e = assertThrows(IllegalStateException.class, () -> deposit.deposit(100));
        assertEquals(ErrorMessages.DEPOSIT_CLOSED, e.getMessage());
    }

    @Test
    @DisplayName("После закрытия вклада, баланс остается таким же")
    void correctBalanceAfterCloseDeposit() {
        deposit.close();
        assertEquals(1000, deposit.getBalance());
    }

    @Test
    @DisplayName("Пополнить вклад, после закрыть его и баланс остается таким же")
    void depositAndCloseThenCorrectBalance() {
        deposit.deposit(100);
        deposit.close();
        assertEquals(1100, deposit.getBalance());
    }

    @Test
    @DisplayName("Нельзя открыть вклад с отрицательным начальным балансом")
    void shouldThrowExceptionWhenCreatingCardWithNegativeInitialBalance() {
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> new Deposit(TestConstants.DEPOSIT_NAME,
                        TestConstants.CURRENCY_RUB,
                        -1));
        assertEquals(ErrorMessages.NEGATIVE_BALANCE, e.getMessage());
    }
}
