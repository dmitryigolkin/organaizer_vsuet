import java.time.LocalDate;

public abstract class Event {
    protected String title;
    protected LocalDate date;
    protected String note;

    public Event(String title, LocalDate date, String note) {
        this.title = title;
        this.date = date;
        this.note = note;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public abstract String getType(); // тип события

    public String getDetails() {
        return "Тип: " + getType()
                + "\nНазвание: " + title
                + "\nДата: " + date
                + "\nЗаметка: " + (note.isEmpty() ? "—" : note);
    }
}

class Birthday extends Event {
    private String personName;

    public Birthday(String title, LocalDate date, String note, String personName) {
        super(title, date, note);
        this.personName = personName;
    }

    public String getPersonName() { return personName; }
    public void setPersonName(String personName) { this.personName = personName; }

    @Override
    public String getType() { return "День рождения"; }

    @Override
    public String getDetails() {
        return super.getDetails() + "\nИменинник: " + personName;
    }
}

class Meeting extends Event {
    private String place;
    private String participants;

    public Meeting(String title, LocalDate date, String note, String place, String participants) {
        super(title, date, note);
        this.place = place;
        this.participants = participants;
    }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public String getParticipants() { return participants; }
    public void setParticipants(String participants) { this.participants = participants; }

    @Override
    public String getType() { return "Важная встреча"; }

    @Override
    public String getDetails() {
        return super.getDetails()
                + "\nМесто: " + (place.isEmpty() ? "—" : place)
                + "\nУчастники: " + (participants.isEmpty() ? "—" : participants);
    }
}
