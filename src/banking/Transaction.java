package banking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс, представляющий банковскую транзакцию
 */
public class Transaction {
    private final LocalDateTime timestamp;
    private final TransactionType type;
    private final double amount;
    private final String description;

    // Форматтер для красивого вывода даты и времени
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public Transaction(TransactionType type, double amount, String description) {
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    // Геттеры
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %.2f руб. - %s",
                timestamp.format(FORMATTER),
                type.getDescription(),
                amount,
                description);
    }
}