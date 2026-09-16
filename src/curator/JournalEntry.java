package curator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Сутність "Запис в журналі куратора":
 * прізвище, ім'я, дата народження, телефон та домашня адреса студента.
 */
public class JournalEntry {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.uuuu");

    private final String lastName;
    private final String firstName;
    private final LocalDate birthDate;
    private final String phone;
    private final Address address;

    public JournalEntry(String lastName, String firstName, LocalDate birthDate,
                         String phone, Address address) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.phone = phone;
        this.address = address;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return String.format(
                "%s %s | дата народження: %s | тел.: %s | адреса: %s",
                lastName, firstName, birthDate.format(DATE_FORMAT), phone, address);
    }
}
