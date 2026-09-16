package curator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * Консольна програма "Журнал куратора".
 * Дозволяє додавати записи про студентів з перевіркою коректності введення
 * (повторне введення при помилці) та переглядати всі записи журналу.
 */
public class Main {

    private final Scanner scanner = new Scanner(System.in);
    private final List<JournalEntry> journal = new ArrayList<>();

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        boolean working = true;
        while (working) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addEntry();
                case "2" -> showAllEntries();
                case "0" -> {
                    System.out.println("До побачення!");
                    working = false;
                }
                default -> System.out.println("Невідома команда. Спробуйте ще раз.\n");
            }
        }
    }

    private void printMenu() {
        System.out.println("==== Журнал куратора ====");
        System.out.println("1 - Додати запис");
        System.out.println("2 - Показати всі записи");
        System.out.println("0 - Вихід");
        System.out.print("Ваш вибір: ");
    }

    private void addEntry() {
        System.out.println("\n--- Додавання нового запису ---");

        String lastName = readValidated(
                "Прізвище студента: ",
                Validator::isValidName,
                "Прізвище має містити від 2 до 30 літер (українських або латинських).");

        String firstName = readValidated(
                "Ім'я студента: ",
                Validator::isValidName,
                "Ім'я має містити від 2 до 30 літер (українських або латинських).");

        LocalDate birthDate = readValidatedDate(
                "Дата народження (дд.мм.рррр): ",
                "Некоректна дата. Використовуйте формат дд.мм.рррр, дата не може бути у майбутньому.");

        String phone = readValidated(
                "Телефон (+380XXXXXXXXX): ",
                Validator::isValidPhone,
                "Телефон має бути у форматі +380 та 9 цифр, наприклад +380501234567.");

        String street = readValidated(
                "Вулиця: ",
                Validator::isValidStreet,
                "Назва вулиці має містити від 2 до 50 символів.");

        String house = readValidated(
                "Будинок: ",
                Validator::isValidHouse,
                "Номер будинку у форматі, наприклад: 12, 12А або 12/3.");

        String apartment = readValidated(
                "Квартира (залиште порожнім, якщо приватний будинок): ",
                Validator::isValidApartment,
                "Номер квартири має складатися лише з цифр.");

        Address address = new Address(street, house, apartment);
        JournalEntry entry = new JournalEntry(lastName, firstName, birthDate, phone, address);
        journal.add(entry);

        System.out.println("Запис успішно додано!\n");
    }

    private void showAllEntries() {
        System.out.println("\n--- Записи журналу ---");
        if (journal.isEmpty()) {
            System.out.println("Журнал порожній.\n");
            return;
        }
        for (int i = 0; i < journal.size(); i++) {
            System.out.println((i + 1) + ". " + journal.get(i));
        }
        System.out.println();
    }

    /**
     * Читає рядок з консолі, доки він не пройде перевірку валідатором.
     */
    private String readValidated(String prompt, Predicate<String> validator, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (validator.test(input)) {
                return input.trim();
            }
            System.out.println("Помилка: " + errorMessage + " Спробуйте ще раз.");
        }
    }

    /**
     * Читає дату народження з консолі, доки вона не буде коректною.
     */
    private LocalDate readValidatedDate(String prompt, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            LocalDate date = Validator.parseBirthDate(input);
            if (date != null) {
                return date;
            }
            System.out.println("Помилка: " + errorMessage + " Спробуйте ще раз.");
        }
    }
}
