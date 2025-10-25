package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Класс для эффективного поиска банковских счетов по различным атрибутам
 */
public class BankAccountSearch {

    /**
     * Поиск счетов по предикату (условию)
     */
    public static List<BankAccount> search(List<BankAccount> accounts,
                                           Predicate<BankAccount> condition) {
        List<BankAccount> result = new ArrayList<>();
        for (BankAccount account : accounts) {
            if (condition.test(account)) {
                result.add(account);
            }
        }
        return result;
    }

    /**
     * Поиск по номеру счета (точное совпадение)
     */
    public static List<BankAccount> searchByAccountNumber(List<BankAccount> accounts,
                                                          String accountNumber) {
        return search(accounts, account ->
                account.getAccountNumber().equals(accountNumber));
    }

    /**
     * Поиск по БИК (точное совпадение)
     */
    public static List<BankAccount> searchByBik(List<BankAccount> accounts, String bik) {
        return search(accounts, account -> account.getBik().equals(bik));
    }

    /**
     * Поиск по КПП (точное совпадение)
     */
    public static List<BankAccount> searchByKpp(List<BankAccount> accounts, String kpp) {
        return search(accounts, account -> account.getKpp().equals(kpp));
    }

    /**
     * Поиск по имени владельца (частичное совпадение, без учета регистра)
     */
    public static List<BankAccount> searchByOwnerName(List<BankAccount> accounts,
                                                      String ownerName) {
        return search(accounts, account ->
                account.getOwnerName().toLowerCase().contains(ownerName.toLowerCase()));
    }

    /**
     * Поиск по ИНН (точное совпадение)
     */
    public static List<BankAccount> searchByInn(List<BankAccount> accounts, String inn) {
        return search(accounts, account ->
                inn.equals(account.getInn())); // ИНН может быть null
    }

    /**
     * Поиск по диапазону баланса
     */
    public static List<BankAccount> searchByBalanceRange(List<BankAccount> accounts,
                                                         double minBalance, double maxBalance) {
        return search(accounts, account ->
                account.getBalance() >= minBalance && account.getBalance() <= maxBalance);
    }

    /**
     * Комплексный поиск по нескольким параметрам
     */
    public static List<BankAccount> advancedSearch(List<BankAccount> accounts,
                                                   String accountNumber, String bik,
                                                   String kpp, String ownerName) {
        Predicate<BankAccount> condition = account -> true;

        if (accountNumber != null && !accountNumber.isEmpty()) {
            condition = condition.and(account ->
                    account.getAccountNumber().contains(accountNumber));
        }

        if (bik != null && !bik.isEmpty()) {
            condition = condition.and(account -> account.getBik().contains(bik));
        }

        if (kpp != null && !kpp.isEmpty()) {
            condition = condition.and(account -> account.getKpp().contains(kpp));
        }

        if (ownerName != null && !ownerName.isEmpty()) {
            condition = condition.and(account ->
                    account.getOwnerName().toLowerCase().contains(ownerName.toLowerCase()));
        }

        return search(accounts, condition);
    }
}