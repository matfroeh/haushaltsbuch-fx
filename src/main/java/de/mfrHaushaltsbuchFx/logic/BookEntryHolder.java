package de.mfrHaushaltsbuchFx.logic;

import de.mfrHaushaltsbuchFx.logic.db.DbManager;
import de.mfrHaushaltsbuchFx.model.BookEntry;
import de.mfrHaushaltsbuchFx.model.EntryType;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * Singleton. Stellt eine ObservableList mit den aus der DB-Tabelle ausgelesenen Einträgen bereit und gibt Veränderungen in den
 * Attributen der {@link BookEntry}-Objekten an die DB über den {@link DbManager} weiter.
 */
public class BookEntryHolder {
    //region Konstanten
    //endregion

    //region Attribute
    private static BookEntryHolder instance;
    private ObservableList<BookEntry> accountingBook;
//    private ObservableList<String> customCategoriesList;
    //endregion

    //region Konstruktoren
    private BookEntryHolder() {
        accountingBook = FXCollections.observableArrayList(bookEntry -> new Observable[]{
                bookEntry.getEntryType().typeProperty(),
                bookEntry.getEntryType().intervalProperty(),
                bookEntry.amountProperty(), bookEntry.dateProperty(),
                bookEntry.categoryProperty(), bookEntry.descriptionProperty()
        });

        // Alle Daten aus DB-Tabelle über den Manager holen und in die ObservableList einfügen
        accountingBook.addAll(DbManager.getInstance().getAllBookEntries());

        // Veränderungen in den Einträgen der ObservableList an DB weitergeben
        accountingBook.addListener(new ListChangeListener<BookEntry>() {
            @Override
            public void onChanged(Change<? extends BookEntry> changes) {

                while (changes.next()) {
                    //System.out.println(changes);

                    if (changes.wasAdded()) {
                        //SubList kann in der derzeit verwendeten Steuerlogik nur ein Element enthalten
                        BookEntry newBookEntry = changes.getAddedSubList().get(0);
                        DbManager.getInstance().insertData(newBookEntry);

                    } else if (changes.wasUpdated()) {
                        int indexOfUpdatedEntry = changes.getFrom();
                        BookEntry updatedBookEntry = changes.getList().get(indexOfUpdatedEntry);

                        DbManager.getInstance().updateBookEntry(updatedBookEntry);

                    } else if (changes.wasRemoved()) {
                        BookEntry bookEntryToRemove = changes.getRemoved().get(0);
                        DbManager.getInstance().deleteBookEntry(bookEntryToRemove);

                    }
                }
            };
        });
    }
    //endregion

    //region Methoden
    /**
     * Erstellt Singleton des BookEntryHolders.
     * @return BookEntryHolder
     */
    public static synchronized BookEntryHolder getInstance() {
        if (instance == null) instance = new BookEntryHolder();
        return instance;
    }

    //AUSGELAGERT IN EIGENE KLASSE CustomCategoriesListHandler
//    /**
//     * Erstellt anhand der in der Liste aus Buchungseinträgen (=AccountingBook) auffindbaren Kategorien der jeweiligen Einträge
//     * eine ObservableList mit den Kategorien als String. Die als Parameter übergebene ObservableList dient
//     * dabei als Ausschlussliste.
//     * @param existingList eine Liste mit den bereits existierenden bzw.
//     *                    auszuschließenden Kategorien (z.B. die "Werkseinstellungs"-Kategorien, die in der fxml des
//     *                     GUI definiert sind)
//     * @return eine ObservableList mit String-Objekten, die die jeweiligen Kategorien an Buchungen repräsentieren
//     */
//    public ObservableList<String> getCustomCategoriesList(ObservableList existingList) {
//        customCategoriesList = FXCollections.observableArrayList();
//
//        for (BookEntry entry : accountingBook) {
//
//            if (!customCategoriesList.contains(entry.getCategory()) && !existingList.contains(entry.getCategory())) {
//                customCategoriesList.add(entry.getCategory());
//            }
//        }
//        return customCategoriesList;
//    }

    /**
     * Liefert die ObservableLis aus Buchungseinträgen (=AccountingBook) zurück.
     * @return
     */
    public ObservableList<BookEntry> getAccountingBook() {
        return accountingBook;
    }
    //endregion

}