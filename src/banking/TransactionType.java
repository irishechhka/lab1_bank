package banking;

/**
 * Перечисление типов банковских транзакций
 */
public enum TransactionType {
    DEPOSIT("Пополнение"),
    WITHDRAWAL("Снятие"),
    OPEN_ACCOUNT("Открытие счета");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}