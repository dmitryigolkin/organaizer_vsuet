import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Organizer organizer = new Organizer();

    public static void main(String[] args) {
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addEvent();
                case "2" -> deleteEvent();
                case "3" -> viewEvent();
                case "4" -> editEvent();
                case "5" -> listEvents();
                case "0" -> {
                    System.out.println("Выход.");
                    return;
                }
                default -> System.out.println("Неизвестная команда.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n===== ОРГАНАЙЗЕР =====");
        System.out.println("1 - Добавить событие");
        System.out.println("2 - Удалить событие");
        System.out.println("3 - Подробный просмотр события");
        System.out.println("4 - Редактировать событие");
        System.out.println("5 - Список событий");
        System.out.println("0 - Выход");
        System.out.print("Выберите действие: ");
    }

    private static LocalDate readDate() {
        while (true) {
            System.out.print("Дата (формат ГГГГ-ММ-ДД): ");
            try {
                return LocalDate.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Неверный формат даты, попробуйте ещё раз.");
            }
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число.");
            }
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static void addEvent() {
        int type = readInt("Тип события: 1 - день рождения, 2 - встреча: ");
        String title = readLine("Название: ");
        LocalDate date = readDate();
        String note = readLine("Заметка (можно пропустить): ");

        if (type == 1) {
            String name = readLine("Имя именинника: ");
            organizer.add(new Birthday(title, date, note, name));
        } else {
            String place = readLine("Место встречи: ");
            String people = readLine("Участники (через запятую): ");
            organizer.add(new Meeting(title, date, note, place, people));
        }
        System.out.println("Событие добавлено.");
    }

    // Поиск дня с событиями по году/месяцу/дню, возвращает индекс выбранного события или -1
    private static int selectEvent() {
        organizer.printYears();
        if (organizer.isEmptyForTest()) return -1;

        int year = readInt("Год: ");
        int month = readInt("Месяц (1-12): ");
        organizer.printMonth(year, month);

        List<Event> day = organizer.getDay(LocalDate.of(year, month, readInt("День: ")));
        if (day == null || day.isEmpty()) {
            System.out.println("В этот день событий нет.");
            return -1;
        }
        int index = readInt("Номер события: ");
        if (index < 0 || index >= day.size()) {
            System.out.println("Неверный номер.");
            return -1;
        }
        return index;
    }

    private static void deleteEvent() {
        int year = readInt("Год: ");
        int month = readInt("Месяц (1-12): ");
        int day = readInt("День: ");
        LocalDate date = LocalDate.of(year, month, day);
        List<Event> events = organizer.getDay(date);
        if (events == null || events.isEmpty()) {
            System.out.println("В этот день событий нет.");
            return;
        }
        for (int i = 0; i < events.size(); i++) {
            System.out.println("[" + i + "] " + events.get(i).getType() + ": " + events.get(i).getTitle());
        }
        int idx = readInt("Номер события для удаления: ");
        if (organizer.delete(date, idx)) {
            System.out.println("Событие удалено.");
        } else {
            System.out.println("Неверный номер.");
        }
    }

    private static void viewEvent() {
        int year = readInt("Год: ");
        int month = readInt("Месяц (1-12): ");
        int day = readInt("День: ");
        List<Event> events = organizer.getDay(LocalDate.of(year, month, day));
        if (events == null || events.isEmpty()) {
            System.out.println("В этот день событий нет.");
            return;
        }
        for (int i = 0; i < events.size(); i++) {
            System.out.println("[" + i + "] " + events.get(i).getType() + ": " + events.get(i).getTitle());
        }
        int idx = readInt("Номер события для просмотра: ");
        if (idx >= 0 && idx < events.size()) {
            System.out.println(events.get(idx).getDetails());
        } else {
            System.out.println("Неверный номер.");
        }
    }

    private static void editEvent() {
        int year = readInt("Год: ");
        int month = readInt("Месяц (1-12): ");
        int day = readInt("День: ");
        LocalDate date = LocalDate.of(year, month, day);
        List<Event> events = organizer.getDay(date);
        if (events == null || events.isEmpty()) {
            System.out.println("В этот день событий нет.");
            return;
        }
        for (int i = 0; i < events.size(); i++) {
            System.out.println("[" + i + "] " + events.get(i).getType() + ": " + events.get(i).getTitle());
        }
        int idx = readInt("Номер события для редактирования: ");
        if (idx < 0 || idx >= events.size()) {
            System.out.println("Неверный номер.");
            return;
        }
        Event e = events.get(idx);

        String newTitle = readLine("Новое название (Enter - оставить \"" + e.getTitle() + "\"): ");
        if (!newTitle.isEmpty()) e.setTitle(newTitle);

        String newDate = readLine("Новая дата (ГГГГ-ММ-ДД, Enter - без изменений): ");
        if (!newDate.isEmpty()) {
            try {
                LocalDate d = LocalDate.parse(newDate);
                organizer.delete(date, idx);
                e.setDate(d);
                organizer.add(e);
                System.out.println("Дата изменена.");
                return;
            } catch (DateTimeParseException ex) {
                System.out.println("Неверный формат, дата не изменена.");
            }
        }

        String newNote = readLine("Новая заметка (Enter - оставить \"" + e.getNote() + "\"): ");
        if (!newNote.isEmpty()) e.setNote(newNote);

        if (e instanceof Birthday b) {
            String name = readLine("Новое имя именинника (Enter - оставить \"" + b.getPersonName() + "\"): ");
            if (!name.isEmpty()) b.setPersonName(name);
        } else if (e instanceof Meeting m) {
            String place = readLine("Новое место (Enter - оставить \"" + m.getPlace() + "\"): ");
            if (!place.isEmpty()) m.setPlace(place);
            String people = readLine("Новые участники (Enter - оставить \"" + m.getParticipants() + "\"): ");
            if (!people.isEmpty()) m.setParticipants(people);
        }
        System.out.println("Событие обновлено.");
    }

    private static void listEvents() {
        int year = readInt("Год: ");
        int month = readInt("Месяц (1-12): ");
        organizer.printMonth(year, month);
    }
}
