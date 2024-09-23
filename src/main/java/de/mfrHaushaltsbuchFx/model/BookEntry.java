package de.mfrHaushaltsbuchFx.model;

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * Die {@code BookEntry} Klasse repräsentiert Buchungseinträge. Diese weisen einen Betrag, ein Buchungsdatum, eine Kategorie,
 * eine Beschreibung des Eintrags und eine zum Buchungseintrag dazugehörige Buchungsart (EntryType) auf.
 * <p>
 * Die Buchungsarten (Einnahmen, Ausgaben, ...) werden dabei von der Klasse {@link EntryType} definiert.
 * Dies soll eine vereinfachte Erweiterung/Anpassung an unterschiedliche Buchungsarten ermöglichen.
 * </p>
 */
public class BookEntry {
    //region Konstanten
    //endregion

    //region Attribute
    private DoubleProperty amount;
    private ObjectProperty<LocalDate> date;
    private StringProperty category;
    private StringProperty description;
    private EntryType entryType;
    private int id;
    //endregion

    //region Konstruktoren
    /**
     * Erzeugt einen neuen {@link BookEntry} und einen zugehörigen {@link EntryType} über die Angabe des entryTypeIndex.
     * Interval des EntryTypes wird unabhängig der Buchungsart auf den Standardwert interval = 0 gesetzt.
     * @param entryTypeIndex Index in der Liste der Buchungsarten {@link EntryType#LIST_OF_BOOKING_TYPES}
     * @param amount Betrag der Buchung
     * @param date Datum der Buchung
     * @param category Kategorie der Buchung
     * @param description Beschreibung der Buchung
     */
    public BookEntry(int entryTypeIndex, double amount, LocalDate date, String category,
                     String description) {
        this.entryType = new EntryType(entryTypeIndex);
        this.amount = new SimpleDoubleProperty(amount);
        this.date = new SimpleObjectProperty<>(date);
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
    }

    /**
     * Erzeugt einen neuen {@link BookEntry} über die Verwendung eines übergebenen {@link EntryType}.
     * @param entryType {@link EntryType}-Instanz
     * @param amount Betrag der Buchung
     * @param date Datum der Buchung
     * @param category Kategorie der Buchung
     * @param description Beschreibung der Buchung
     */
    public BookEntry(EntryType entryType, double amount, LocalDate date, String category,
                     String description) {
        this.entryType = entryType;
        this.amount = new SimpleDoubleProperty(amount);
        this.date = new SimpleObjectProperty<>(date);
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
    }
    //endregion

    //region Methoden
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public double getAmount() {
        return amount.get();
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public String getCategory() {
        return category.get();
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    @Override
    public String toString() {
        return  "Datum: " + date.get() +
                ", Betrag: " + amount.get() + " €" +
                ", Kategorie: " + category.get() +
                ", Beschreibung: " + description.get() +
                ", " + entryType.toString();

    }

//endregion


}
