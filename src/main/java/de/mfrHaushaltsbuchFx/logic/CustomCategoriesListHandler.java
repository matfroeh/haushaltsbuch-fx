package de.mfrHaushaltsbuchFx.logic;

import de.mfrHaushaltsbuchFx.model.BookEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Klasse mit statischer Methode zum Erstellen einer Liste der "benutzerdefinierten Kategorien" für den Kategorienwähler
 * {@link de.mfrHaushaltsbuchFx.gui.BudgetOverviewController#categoryChoiceBoxForNewEntry} der GUI.
 * "Benutzerdefiniert" bedeutet hier: Kategorien, die in den DB-Einträgen bereits verwendet werden, aber nicht Teil der
 * in der fxml definierten Liste der categoryChoiceBox sind.
 */
public class CustomCategoriesListHandler {

    //region Konstruktoren
    private CustomCategoriesListHandler(){
    }
    //endregion

    //region Methoden
    /**
     * Erstellt anhand der in der Liste aus Buchungseinträgen (=AccountingBook) auffindbaren Kategorien der jeweiligen Einträge
     * eine ObservableList mit den Kategorien als String. Die als Parameter übergebene ObservableList dient
     * dabei als Ausschlussliste.
     * @param existingList eine Liste mit den bereits existierenden bzw.
     *                    auszuschließenden Kategorien (z.B. die "Werkseinstellungs"-Kategorien, die in der fxml des
     *                     GUI definiert sind)
     * @return eine ObservableList mit String-Objekten, die die jeweiligen Kategorien an Buchungen repräsentieren
     */
    public static ObservableList<String> getCustomCategoriesList(ObservableList existingList) {
        ObservableList<String> customCategoriesList = FXCollections.observableArrayList();

        for (BookEntry entry : BookEntryHolder.getInstance().getAccountingBook()) {

            if (!customCategoriesList.contains(entry.getCategory()) && !existingList.contains(entry.getCategory())) {
                customCategoriesList.add(entry.getCategory());
            }
        }
        return customCategoriesList;
    }
    //endregion
}
