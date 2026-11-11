import java.time.LocalDate;
import java.util.*;

public class Organizer {
    // key: год -> месяц -> день -> список событий
    private final Map<Integer, Map<Integer, Map<Integer, List<Event>>>> storage = new TreeMap<>();

    public void add(Event e) {
        storage.computeIfAbsent(e.getDate().getYear(), y -> new TreeMap<>())
                .computeIfAbsent(e.getDate().getMonthValue(), m -> new TreeMap<>())
                .computeIfAbsent(e.getDate().getDayOfMonth(), d -> new ArrayList<>())
                .add(e);
    }

    public boolean delete(LocalDate date, int index) {
        List<Event> day = getDay(date);
        if (day == null || index < 0 || index >= day.size()) return false;
        day.remove(index);
        if (day.isEmpty()) {
            storage.get(date.getYear()).get(date.getMonthValue()).remove(date.getDayOfMonth());
        }
        return true;
    }

    public List<Event> getDay(LocalDate date) {
        return storage.getOrDefault(date.getYear(), Collections.emptyMap())
                .getOrDefault(date.getMonthValue(), Collections.emptyMap())
                .get(date.getDayOfMonth());
    }

    // Вывод всех событий за месяц
    public void printMonth(int year, int month) {
        Map<Integer, List<Event>> monthMap =
                storage.getOrDefault(year, Collections.emptyMap())
                        .getOrDefault(month, Collections.emptyMap());
        if (monthMap.isEmpty()) {
            System.out.println("Событий за этот месяц нет.");
            return;
        }
        for (Map.Entry<Integer, List<Event>> e : monthMap.entrySet()) {
            System.out.println("=== " + year + "-" + String.format("%02d", month) + "-" + String.format("%02d", e.getKey()) + " ===");
            List<Event> list = e.getValue();
            for (int i = 0; i < list.size(); i++) {
                System.out.println("  [" + i + "] " + list.get(i).getType() + ": " + list.get(i).getTitle());
            }
        }
    }

    // Вывод всех лет с событиями
    public void printYears() {
        if (storage.isEmpty()) {
            System.out.println("Органайзер пуст.");
            return;
        }
        System.out.println("Годы с событиями: " + storage.keySet());
    }

    public boolean isEmptyForTest() {
        return storage.isEmpty();
    }

}
