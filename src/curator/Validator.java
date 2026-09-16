package curator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.Pattern;

/**
 * Перевірка коректності введених даних за допомогою регулярних виразів.
 * Кожен метод повертає true/false; для дати передбачений окремий метод
 * розбору, який повертає null, якщо рядок не є коректною датою.
 */
public final class Validator {

    // Прізвище/ім'я: 2-30 літер (укр./лат.), апостроф чи дефіс (Анна-Марія, О'Коннор)
    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[A-Za-zА-ЯҐЄІЇа-яґєіїЁёЙй'’\\-]{2,30}$");

    // Телефон у форматі +380XXXXXXXXX (9 цифр після коду країни)
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+380\\d{9}$");

    // Вулиця: 2-50 символів, літери/цифри/пробіли/крапки/дефіси
    private static final Pattern STREET_PATTERN =
            Pattern.compile("^[A-Za-zА-ЯҐЄІЇа-яґєіїЁё0-9 .\\-]{2,50}$");

    // Будинок: цифри, можлива літера і/або корпус через дріб (12, 12А, 12/3)
    private static final Pattern HOUSE_PATTERN =
            Pattern.compile("^\\d{1,4}[A-Za-zА-Яа-я]?(/\\d{1,4})?$");

    // Квартира: тільки цифри; поле необов'язкове (може бути порожнім)
    private static final Pattern APARTMENT_PATTERN =
            Pattern.compile("^\\d{1,5}$");

    // ResolverStyle.STRICT потрібен, щоб "31.02.2024" відхилялося як помилка,
    // а не мовчки перетворювалося на 29.02.2024 (поведінка за замовчуванням - SMART).
    // Використовуємо "uuuu" (рік) замість "yyyy" (рік ери) - у STRICT-режимі
    // "yyyy" вимагає ще й вказану еру і завжди завершується помилкою розбору.
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter
            .ofPattern("dd.MM.uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    private Validator() {
    }

    public static boolean isValidName(String value) {
        return value != null && NAME_PATTERN.matcher(value.trim()).matches();
    }

    public static boolean isValidPhone(String value) {
        return value != null && PHONE_PATTERN.matcher(value.trim()).matches();
    }

    public static boolean isValidStreet(String value) {
        return value != null && STREET_PATTERN.matcher(value.trim()).matches();
    }

    public static boolean isValidHouse(String value) {
        return value != null && HOUSE_PATTERN.matcher(value.trim()).matches();
    }

    /**
     * Квартира - поле необов'язкове: порожній рядок теж вважається коректним
     * (наприклад, для приватного будинку).
     */
    public static boolean isValidApartment(String value) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return APARTMENT_PATTERN.matcher(value.trim()).matches();
    }

    /**
     * Розбирає дату у форматі дд.мм.рррр.
     * Повертає null, якщо формат невірний, дата у майбутньому
     * або відповідає віку понад 120 років.
     */
    public static LocalDate parseBirthDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(value.trim(), DATE_FORMAT);
            LocalDate today = LocalDate.now();
            if (date.isAfter(today) || date.isBefore(today.minusYears(120))) {
                return null;
            }
            return date;
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
