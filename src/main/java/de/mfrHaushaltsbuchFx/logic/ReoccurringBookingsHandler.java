package de.mfrHaushaltsbuchFx.logic;

import de.mfrHaushaltsbuchFx.model.BookEntry;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Klasse mit statischen Methoden zur Handhabung der "wiederkehrenden Buchungseinträge".
 */
public class ReoccurringBookingsHandler {
    //region Konstanten
    //endregion

    //region Attribute
    //endregion

    //region Konstruktoren
    private ReoccurringBookingsHandler() {
    }
    //endregion

    //region Methoden

    /**
     * Erstellt eine ArrayListe mit LocalDate Objekten, die künftige Buchungstermine eines übergebenen
     * BookEntry bis hin zu einem übergebenen End-Datum repräsentieren.
     * @param entry ein {@link BookEntry}, zu dem die wiederkehrenden Buchungstermine abgefragt werden sollen
     * @param endDate End-Datum, bis zu dem (inklusive) alle wiederkehrenden Buchungstermine ausgegeben werden sollen
     * @return eine {@link ArrayList} mit {@link LocalDate}-Objekten
     */
    public static ArrayList<LocalDate> getAllDatesOfFutureReoccurrences(BookEntry entry, LocalDate endDate) {

        ArrayList<LocalDate> datesList = new ArrayList<>();

        LocalDate firstOccurrence = entry.getDate();
        int interval = entry.getEntryType().getInterval();

        int multiplier = 1;

        while (firstOccurrence.plusMonths((long) interval * multiplier).isBefore(endDate)
        || firstOccurrence.plusMonths((long) interval * multiplier).isEqual(endDate) ) {

            datesList.add(firstOccurrence.plusMonths((long) interval * multiplier));
            multiplier++;
        }
        return datesList;
    }

    /**
     * Erstellt mithilfe einem als Parameter übergebenen Buchungseintrag {@link BookEntry}
     * und einer LocalDate-ArrayList (siehe {@link ReoccurringBookingsHandler#getAllDatesOfFutureReoccurrences(BookEntry, LocalDate)})
     * der künftigen Buchungstermine die daraus resultierenden künftigen Buchungseinträge und gibt sie als ArrayList zurück.
     * @param entry ein BookEntry, zu dem alle wiederkehrenden Buchungstermine erstellt werden sollen
     * @param datesList eine ArrayListe mit LocalDate Objekten, die künftige Buchungstermine eines
     *                  übergebenen BookEntry bis hin zu einem übergebenen End-Datum repräsentieren
     * @return eine ArrayList mit {@link BookEntry}-Objekten der künftigen Buchungseinträge
     */
    public static ArrayList<BookEntry> createEntriesBasedOnLocalDateList (BookEntry entry, ArrayList<LocalDate> datesList) {

        ArrayList<BookEntry> entriesList = new ArrayList<>();

        for (LocalDate date : datesList) {

            BookEntry newEntry = new BookEntry(
                    entry.getEntryType(),
                    entry.getAmount(),
                    date,
                    entry.getCategory(),
                    entry.getDescription());

            entriesList.add(newEntry);
        }
        return entriesList;
    }
    //endregion
}
