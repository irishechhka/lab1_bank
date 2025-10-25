package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс, представляющий банковский счет со всеми реквизитами
 */
public class BankAccount {
    private final String accountNumber;     // Номер счета (20 цифр)
    private final String bik;               // БИК банка (9 цифр)
    private final String kpp;               // КПП организации (9 цифр)
    private final String correspondentAccount; // Корреспондентский счет (20 цифр)
    private final String inn;               // ИНН владельца (10 или 12 цифр)
    private final String ownerName;         // Имя владельца счета

    private double balance;
    private final List<Transaction> transactions;
    private final String openDate;

    // Константы для валидации
    private static final int ACCOUNT_NUMBER_LENGTH = 20;
    private static final int BIK_LENGTH = 9;
    private static final int KPP_LENGTH = 9;
    private static final int CORR_ACCOUNT_LENGTH = 20;

    public BankAccount(String accountNumber, String bik, String kpp,
                       String correspondentAccount, String inn, String ownerName,
                       double initialBalance) {
        validateInput(accountNumber, bik, kpp, correspondentAccount, inn);

        this.accountNumber = accountNumber;
        this.bik = bik;
        this.kpp = kpp;
        this.correspondentAccount = correspondentAccount;
        this.inn = inn;
        this.ownerName = ownerName;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
        this.openDate = java.time.LocalDate.now().toString();

        // Добавляем транзакцию открытия счета
        addTransaction(new Transaction(TransactionType.OPEN_ACCOUNT, initialBalance,
                "Открытие счета с начальным балансом"));
    }

    /**
     * Валидация входных данных
     */
    private void validateInput(String accountNumber, String bik, String kpp,
                               String correspondentAccount, String inn) {
        Objects.requireNonNull(accountNumber, "Номер счета не может быть null");
        Objects.requireNonNull(bik, "БИК не может быть null");
        Objects.requireNonNull(kpp, "КПП не может быть null");

        if (!accountNumber.matches("\\d{" + ACCOUNT_NUMBER_LENGTH + "}")) {
            throw new IllegalArgumentException("Номер счета должен содержать " +
                    ACCOUNT_NUMBER_LENGTH + " цифр");
        }

        if (!bik.matches("\\d{" + BIK_LENGTH + "}")) {
            throw new IllegalArgumentException("БИК должен содержать " +
                    BIK_LENGTH + " цифр");
        }

        if (!kpp.matches("\\d{" + KPP_LENGTH + "}")) {
            throw new IllegalArgumentException("КПП должен содержать " +
                    KPP_LENGTH + " цифр");
        }

        if (correspondentAccount != null &&
                !correspondentAccount.matches("\\d{" + CORR_ACCOUNT_LENGTH + "}")) {
            throw new IllegalArgumentException("Корреспондентский счет должен содержать " +
                    CORR_ACCOUNT_LENGTH + " цифр");
        }
    }

    /**
     * Пополнение счета
     */
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма пополнения должна быть положительной");
        }

        balance += amount;
        addTransaction(new Transaction(TransactionType.DEPOSIT, amount,
                "Пополнение счета"));
    }

    /**
     * Снятие со счета
     */
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма снятия должна быть положительной");
        }

        if (amount > balance) {
            return false; // Недостаточно средств
        }

        balance -= amount;
        addTransaction(new Transaction(TransactionType.WITHDRAWAL, amount,
                "Снятие наличных"));
        return true;
    }

    /**
     * Добавление транзакции в историю
     */
    private void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    // Геттеры
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBik() {
        return bik;
    }

    public String getKpp() {
        return kpp;
    }

    public String getCorrespondentAccount() {
        return correspondentAccount;
    }

    public String getInn() {
        return inn;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions); // Возвращаем копию для инкапсуляции
    }

    public String getOpenDate() {
        return openDate;
    }

    @Override
    public String toString() {
        return String.format(
                "Счет: %s | Владелец: %s | Баланс: %.2f руб.\n" +
                        "БИК: %s | КПП: %s | ИНН: %s\n" +
                        "Корр. счет: %s | Дата открытия: %s",
                accountNumber, ownerName, balance, bik, kpp, inn,
                correspondentAccount, openDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return accountNumber.equals(that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}