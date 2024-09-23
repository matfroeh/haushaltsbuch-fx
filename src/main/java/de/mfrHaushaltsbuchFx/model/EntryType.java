package de.mfrHaushaltsbuchFx.model;

import javafx.beans.property.*;

/**
 * Diese Klasse bietet fest definierte Buchungstypen an, die im Array {@code listOfBookingTypes} definiert sind.
 * Die Buchungsart wird über den {@link EntryType#type} definiert. Zu jeder Buchungsart gehört eine
 * Intervall-Länge als Integer {@link EntryType#interval}, die bei wiederkehrenden Buchungen Verwendung findet
 * und ansonsten auf {@link EntryType#interval} = 0 gesetzt wird.
 * <p>
 * Diese Klasse wird in der Klasse {@link BookEntry} verwendet, um vordefinierte Buchungsarten für einen
 * jeweiligen Buchungseintrag setzen zu können. Das Erweitern um später evtl. benötigte Attribute
 * soll hiermit erleichtert werden.
 * </p>
 */
public class EntryType {

    //region Konstanten
    public static final String[] LIST_OF_BOOKING_TYPES = new String[] {"Ausgaben", "Einnahmen",
            "Wiederkehrende Ausgaben", "Wiederkehrende Einnahmen"};
    public static final int BOOKING_TYPE_INDEX_OF_EXPENSES = 0;
    public static final int BOOKING_TYPE_INDEX_OF_INCOME = 1;
    public static final int BOOKING_TYPE_INDEX_OF_REOCCURRING_EXPENSES = 2;
    public static final int BOOKING_TYPE_INDEX_OF_REOCCURRING_INCOME = 3;
    public static final int DEFAULT_INTERVAL_LENGTH = 0;
    //endregion

    //region Attribute
    private final StringProperty type;
    private final IntegerProperty interval;
    //endregion

    //region Konstruktoren
    /**
     * Initialisiert einen neuen {@link EntryType} und weist ihm über den typeIndex den entsprechenden im
     * {@link EntryType#LIST_OF_BOOKING_TYPES}-Array definierten Typ zu.
     * Standard-Intervall-Länge {@link EntryType#interval} = 0.
     * @param   typeIndex
     *          Der Index im String-Array {@code listOfBookingType}
     */
    public EntryType(int typeIndex) {
        type = new SimpleStringProperty(LIST_OF_BOOKING_TYPES[typeIndex]);
        interval = new SimpleIntegerProperty(DEFAULT_INTERVAL_LENGTH);
    }

    /**
     * Initialisiert einen neuen {@link EntryType} und weist ihm über den typeIndex den entsprechenden im
     * {@link EntryType#LIST_OF_BOOKING_TYPES}-Array definierten Typ zu mit dem im Argument gesetzten Integerwert
     * des {@link EntryType#interval}.
     * @param   typeIndex
     *          Der Index im String-Array {@link EntryType#LIST_OF_BOOKING_TYPES}
     * @param interval Interval-Länge (bei wiederkehrenden Buchungsarten > 0)
     */
    public EntryType(int typeIndex, int interval) {
        type = new SimpleStringProperty(LIST_OF_BOOKING_TYPES[typeIndex]);
        this.interval = new SimpleIntegerProperty(interval);
    }
    //endregion

    //region Methoden

    /**
     * Liefert den {@code int} Index aus dem Array der fest definierten Buchungsarten {@link EntryType#LIST_OF_BOOKING_TYPES}
     * bei Übergabe des String-Namens zurück.
     * @param   typeName
     *          Der String-Name der Buchungsart type
     * @return typeIndex
     *          Index aus dem Array {@link EntryType#LIST_OF_BOOKING_TYPES}
     */
    public static int getIndexOfBookingTypeByName(String typeName) {
        for (int i = 0; i < LIST_OF_BOOKING_TYPES.length; i++) {
            if (LIST_OF_BOOKING_TYPES[i].contentEquals(typeName)) {
                int typeIndex = i;
                return typeIndex;
            }
        }
        return -1;
    }

    public IntegerProperty intervalProperty() {
        return interval;
    }

    public StringProperty typeProperty() {
        return type;
    }

    public String getTypeName() {
        return type.get();
    }

    public void setInterval(int interval) {
        this.interval.set(interval);
    }

    public int getInterval() {
        return interval.get();
    }

    public void setAsExpenses() {
        type.setValue(LIST_OF_BOOKING_TYPES[BOOKING_TYPE_INDEX_OF_EXPENSES]);
    }

    public void setAsIncome() {
        type.setValue(LIST_OF_BOOKING_TYPES[BOOKING_TYPE_INDEX_OF_INCOME]);
    }

    public void setAsReoccurringExpenses() {
        type.setValue(LIST_OF_BOOKING_TYPES[BOOKING_TYPE_INDEX_OF_REOCCURRING_EXPENSES]);
    }

    public void setAsReoccurringIncome() {
        type.setValue(LIST_OF_BOOKING_TYPES[BOOKING_TYPE_INDEX_OF_REOCCURRING_INCOME]);
    }

    public boolean isOfTypeIncome() {
        if (type.get().contentEquals(LIST_OF_BOOKING_TYPES[BOOKING_TYPE_INDEX_OF_INCOME])
                || type.get().contentEquals(LIST_OF_BOOKING_TYPES[BOOKING_TYPE_INDEX_OF_REOCCURRING_INCOME])) {
            return true;
        }
        return false;
    }

    public boolean isOfTypeExpenses() {
        if (type.get().contentEquals(LIST_OF_BOOKING_TYPES[BOOKING_TYPE_INDEX_OF_EXPENSES])
                || type.get().contentEquals(LIST_OF_BOOKING_TYPES[BOOKING_TYPE_INDEX_OF_REOCCURRING_EXPENSES])) {
            return true;
        }
        return false;
    }

    public boolean isOfTypeReoccurring() {
        if (type.get().contentEquals(LIST_OF_BOOKING_TYPES[BOOKING_TYPE_INDEX_OF_REOCCURRING_EXPENSES])
                || type.get().contentEquals(LIST_OF_BOOKING_TYPES[BOOKING_TYPE_INDEX_OF_REOCCURRING_INCOME])) {
            return true;
        }
        return false;
    }

    /**
     * Ausgabe des entryType als String. Intervall-Länge wird hier in Monaten angegeben.
     * @return entryType als String
     */
    @Override
    public String toString() {
        if (type.get().contentEquals(LIST_OF_BOOKING_TYPES[BOOKING_TYPE_INDEX_OF_EXPENSES])) {
            return LIST_OF_BOOKING_TYPES[BOOKING_TYPE_INDEX_OF_EXPENSES];
        } else if (type.get().contentEquals(LIST_OF_BOOKING_TYPES[BOOKING_TYPE_INDEX_OF_INCOME])) {
            return LIST_OF_BOOKING_TYPES[BOOKING_TYPE_INDEX_OF_INCOME];
        } else if (type.get().contentEquals(LIST_OF_BOOKING_TYPES[BOOKING_TYPE_INDEX_OF_REOCCURRING_EXPENSES])) {
            return LIST_OF_BOOKING_TYPES[BOOKING_TYPE_INDEX_OF_REOCCURRING_EXPENSES]
                    + " alle " + interval.get() + " Monate";
        } else if (type.get().contentEquals(LIST_OF_BOOKING_TYPES[BOOKING_TYPE_INDEX_OF_REOCCURRING_INCOME])) {
            return LIST_OF_BOOKING_TYPES[BOOKING_TYPE_INDEX_OF_REOCCURRING_INCOME]
                    + " alle " + interval.get() + " Monate";
        }
        else return "Error printing EntryType toString()";
    }
    //endregion
}
