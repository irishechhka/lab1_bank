package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Основной класс банковской системы с консольным меню
 */
public class BankSystem {
    private final List<BankAccount> accounts;
    private final Scanner scanner;

    public BankSystem() {
        this.accounts = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Главное меню системы
     */
    public void start() {
        System.out.println("=== БАНКОВСКАЯ СИСТЕМА ===");

        while (true) {
            printMenu();
            int choice = readIntInput("Выберите действие: ");

            switch (choice) {
                case 1 -> openAccount();
                case 2 -> depositMoney();
                case 3 -> withdrawMoney();
                case 4 -> showBalance();
                case 5 -> showTransactions();
                case 6 -> searchAccounts();
                case 7 -> showAllAccounts();
                case 0 -> {
                    System.out.println("Выход из системы...");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- МЕНЮ ---");
        System.out.println("1. Открыть счет");
        System.out.println("2. Положить деньги");
        System.out.println("3. Снять деньги");
        System.out.println("4. Показать баланс");
        System.out.println("5. Вывести список транзакций");
        System.out.println("6. Поиск по атрибутам");
        System.out.println("7. Показать все счета");
        System.out.println("0. Выход");
    }

    /**
     * Открытие нового счета
     */
    private void openAccount() {
        System.out.println("\n--- ОТКРЫТИЕ СЧЕТА ---");

        try {
            String accountNumber = readInput("Введите номер счета (20 цифр): ");
            String bik = readInput("Введите БИК банка (9 цифр): ");
            String kpp = readInput("Введите КПП (9 цифр): ");
            String correspondentAccount = readInput("Введите корреспондентский счет (20 цифр, опционально): ");
            String inn = readInput("Введите ИНН владельца (опционально): ");
            String ownerName = readInput("Введите имя владельца: ");
            double initialBalance = readDoubleInput("Введите начальный баланс: ");

            // Если поля опциональные и пустые, устанавливаем null
            if (correspondentAccount.isEmpty()) correspondentAccount = null;
            if (inn.isEmpty()) inn = null;

            BankAccount newAccount = new BankAccount(accountNumber, bik, kpp,
                    correspondentAccount, inn, ownerName, initialBalance);

            accounts.add(newAccount);
            System.out.println("Счет успешно открыт!");
            System.out.println(newAccount);

        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при открытии счета: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }

    /**
     * Пополнение счета
     */
    private void depositMoney() {
        System.out.println("\n--- ПОПОЛНЕНИЕ СЧЕТА ---");

        BankAccount account = findAccountByNumber();
        if (account == null) return;

        double amount = readDoubleInput("Введите сумму для пополнения: ");

        try {
            account.deposit(amount);
            System.out.printf("Счет успешно пополнен на %.2f руб.\n", amount);
            System.out.printf("Текущий баланс: %.2f руб.\n", account.getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Снятие денег со счета
     */
    private void withdrawMoney() {
        System.out.println("\n--- СНЯТИЕ ДЕНЕГ ---");

        BankAccount account = findAccountByNumber();
        if (account == null) return;

        double amount = readDoubleInput("Введите сумму для снятия: ");

        try {
            boolean success = account.withdraw(amount);
            if (success) {
                System.out.printf("Со счета снято %.2f руб.\n", amount);
                System.out.printf("Текущий баланс: %.2f руб.\n", account.getBalance());
            } else {
                System.out.println("Недостаточно средств на счете!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Показать баланс счета
     */
    private void showBalance() {
        System.out.println("\n--- БАЛАНС СЧЕТА ---");

        BankAccount account = findAccountByNumber();
        if (account == null) return;

        System.out.printf("Текущий баланс: %.2f руб.\n", account.getBalance());
    }

    /**
     * Показать историю транзакций
     */
    private void showTransactions() {
        System.out.println("\n--- ИСТОРИЯ ТРАНЗАКЦИЙ ---");

        BankAccount account = findAccountByNumber();
        if (account == null) return;

        List<Transaction> transactions = account.getTransactions();

        if (transactions.isEmpty()) {
            System.out.println("Транзакций не найдено.");
            return;
        }

        System.out.println("История транзакций для счета " + account.getAccountNumber() + ":");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    /**
     * Поиск счетов по атрибутам
     */
    private void searchAccounts() {
        System.out.println("\n--- ПОИСК СЧЕТОВ ---");
        System.out.println("1. По номеру счета");
        System.out.println("2. По БИК");
        System.out.println("3. По КПП");
        System.out.println("4. По имени владельца");
        System.out.println("5. По ИНН");
        System.out.println("6. По диапазону баланса");
        System.out.println("7. Комплексный поиск");

        int choice = readIntInput("Выберите тип поиска: ");

        List<BankAccount> results;

        switch (choice) {
            case 1 -> {
                String accountNumber = readInput("Введите номер счета: ");
                results = BankAccountSearch.searchByAccountNumber(accounts, accountNumber);
            }
            case 2 -> {
                String bik = readInput("Введите БИК: ");
                results = BankAccountSearch.searchByBik(accounts, bik);
            }
            case 3 -> {
                String kpp = readInput("Введите КПП: ");
                results = BankAccountSearch.searchByKpp(accounts, kpp);
            }
            case 4 -> {
                String ownerName = readInput("Введите имя владельца: ");
                results = BankAccountSearch.searchByOwnerName(accounts, ownerName);
            }
            case 5 -> {
                String inn = readInput("Введите ИНН: ");
                results = BankAccountSearch.searchByInn(accounts, inn);
            }
            case 6 -> {
                double minBalance = readDoubleInput("Введите минимальный баланс: ");
                double maxBalance = readDoubleInput("Введите максимальный баланс: ");
                results = BankAccountSearch.searchByBalanceRange(accounts, minBalance, maxBalance);
            }
            case 7 -> {
                String accountNumber = readInput("Введите номер счета (частично или полностью): ");
                String bik = readInput("Введите БИК (частично или полностью): ");
                String kpp = readInput("Введите КПП (частично или полностью): ");
                String ownerName = readInput("Введите имя владельца (частично или полностью): ");
                results = BankAccountSearch.advancedSearch(accounts, accountNumber, bik, kpp, ownerName);
            }
            default -> {
                System.out.println("Неверный выбор.");
                return;
            }
        }

        displaySearchResults(results);
    }

    /**
     * Показать все счета
     */
    private void showAllAccounts() {
        System.out.println("\n--- ВСЕ СЧЕТА ---");

        if (accounts.isEmpty()) {
            System.out.println("Счета не найдены.");
            return;
        }

        for (int i = 0; i < accounts.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, accounts.get(i));
            System.out.println("---");
        }
    }

    /**
     * Вспомогательный метод для поиска счета по номеру
     */
    private BankAccount findAccountByNumber() {
        String accountNumber = readInput("Введите номер счета: ");

        List<BankAccount> foundAccounts = BankAccountSearch.searchByAccountNumber(accounts, accountNumber);

        if (foundAccounts.isEmpty()) {
            System.out.println("Счет с номером " + accountNumber + " не найден.");
            return null;
        }

        return foundAccounts.get(0);
    }

    /**
     * Отображение результатов поиска
     */
    private void displaySearchResults(List<BankAccount> results) {
        if (results.isEmpty()) {
            System.out.println("Счета не найдены.");
            return;
        }

        System.out.println("\nНайдено счетов: " + results.size());
        for (BankAccount account : results) {
            System.out.println(account);
            System.out.println("---");
        }
    }

    // Вспомогательные методы для ввода данных
    private String readInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число.");
            }
        }
    }

    private double readDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число.");
            }
        }
    }
}