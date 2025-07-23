package sberbank.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sberbank.common.ErrorMessages;
import sberbank.common.TestConstants;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты кредитной карты")
public class CreditCardTest {
    private CreditCard creditCard;

    @BeforeEach
    void setUp() {
        creditCard = new CreditCard(
                TestConstants.CREDIT_CARD_NAME,
                TestConstants.CURRENCY_RUB,
                150_000,
                10);
    }

    @Test
    @DisplayName("Снятие уменьшает баланс и увеличивает задолженность с учётом процента")
    void withdrawReducesBalanceAndIncreasesDebtWithInterest() {
        creditCard.withdraw(10000);
        assertEquals(140000, creditCard.getBalance());
        assertEquals(11000, creditCard.getDebt());
    }

    @Test
    @DisplayName("Несколько снятий накапливают задолженность корректно")
    void multipleWithdrawalsAccumulateCorrectDebt() {
        creditCard.withdraw(5000);
        creditCard.withdraw(2000);
        assertEquals(143000, creditCard.getBalance());
        assertEquals(7700, creditCard.getDebt());
    }

    @Test
    @DisplayName("Пополнение уменьшает процентную задолженность и увеличивает баланс")
    void depositReducesDebtAndIncreasesBalance() {
        creditCard.withdraw(10000);
        creditCard.deposit(5000);
        assertEquals(144000, creditCard.getBalance(), 0.01);
        assertEquals(6000, creditCard.getDebt(), 0.01);
    }


    @Test
    @DisplayName("Полное пополнение гасит задолженность")
    void depositFullAmountClearsDebt() {
        creditCard.withdraw(10000);
        creditCard.deposit(11000);
        assertEquals(150000, creditCard.getBalance());
        assertEquals(0, creditCard.getDebt());
    }

    @Test
    @DisplayName("Начальный баланс возвращается корректно")
    void getBalanceReturnsInitialIfNoWithdraw() {
        assertEquals(150000, creditCard.getBalance());
    }

    @Test
    @DisplayName("Задолженность по умолчанию равна 0")
    void getDebtReturnsZeroInitially() {
        assertEquals(0, creditCard.getDebt());
    }

    @Test
    @DisplayName("Нельзя пополнить отрицательной суммой")
    void depositThrowsIfNegative() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> creditCard.deposit(-1));
        assertEquals(ErrorMessages.NEGATIVE_AMOUNT, e.getMessage());
    }

    @Test
    @DisplayName("Нельзя снять отрицательную сумму")
    void withdrawThrowsIfNegative() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> creditCard.withdraw(-1));
        assertEquals(ErrorMessages.NEGATIVE_AMOUNT, e.getMessage());
    }

    @Test
    @DisplayName("Пополнение без долгов не увеличивает задолжность")
    void depositWithoutDebtDoesNothing() {
        creditCard.deposit(1000);
        assertEquals(151000, creditCard.getBalance());
        assertEquals(0, creditCard.getDebt());
    }

    @Test
    @DisplayName("Нельзя создать карту с отрицательной процентной ставкой")
    void cannotCreateCardWithNegativeInterest() {
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> new CreditCard(TestConstants.CREDIT_CARD_NAME,
                        TestConstants.CURRENCY_RUB,
                        10000,
                        -11));
        assertEquals(ErrorMessages.NEGATIVE_RATE, e.getMessage());
    }

    @Test
    @DisplayName("Снятие при балансе выше начального не создаёт долг, если итог ≥ первоначального")
    void withdrawFromAboveInitialBalanceDoesNotCreateDebt() {
        creditCard.deposit(20_000);
        creditCard.withdraw(20_000);
        assertAll(
                () -> assertEquals(150_000, creditCard.getBalance()),
                () -> assertEquals(0, creditCard.getDebt())
        );
    }

    @Test
    @DisplayName("Снятие с переполненной карты до ниже начального баланса уменьшает баланс и создаёт долг")
    void withdrawBelowInitialFromOverflowedBalanceCreatesDebt() {
        creditCard.deposit(20_000);
        creditCard.withdraw(200_000);
        assertAll(
                () -> assertEquals(-30_000, creditCard.getBalance()),
                () -> assertEquals(198_000, creditCard.getDebt())
        );
    }

    @Test
    @DisplayName("Снятие суммы, превышающей изначальный баланс, уводит баланс в минус и увеличивает задолженность.")
    void withdrawMoreThanInitialBalanceShouldReduceBalanceAndIncreaseDebt() {
        creditCard.withdraw(200_000);
        assertAll(
                () -> assertEquals(-50_000, creditCard.getBalance()),
                () -> assertEquals(220_000, creditCard.getDebt())
        );
    }

    @Test
    @DisplayName("Нельзя создать кредитную карту с отрицательным начальным балансом")
    void shouldThrowExceptionWhenCreatingCardWithNegativeInitialBalance(){
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> new CreditCard(TestConstants.CREDIT_CARD_NAME,
                        TestConstants.CURRENCY_RUB,
                        -1,
                        10));
        assertEquals(ErrorMessages.NEGATIVE_BALANCE, e.getMessage());
    }
    @Test
    @DisplayName("Пополнение на сумму меньше процентного долга уменьшает только процентный долг, но не влияет на баланс")
    void depositLessThanInterestDebtShouldReduceOnlyInterestDebt() {
        creditCard.withdraw(50_000);
        creditCard.deposit(4_000);
        assertAll(
                () -> assertEquals(100_000, creditCard.getBalance()),
                () -> assertEquals(51_000, creditCard.getDebt())
        );
    }

    @Test
    @DisplayName("Пополнение карты с долгом на сумму, превышающую изначальный баланс, полностью гасит долг и увеличивает баланс")
    void depositMoreThanInitialBalanceShouldClearDebtAndIncreaseBalance() {
        creditCard.withdraw(50_000);
        creditCard.deposit(100_000);
        assertAll(
                () -> assertEquals(195_000, creditCard.getBalance()),
                () -> assertEquals(0
                        , creditCard.getDebt())
        );
    }
}
