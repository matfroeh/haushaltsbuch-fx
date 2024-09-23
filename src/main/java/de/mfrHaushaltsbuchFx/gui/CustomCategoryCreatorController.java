package de.mfrHaushaltsbuchFx.gui;

import de.mfrHaushaltsbuchFx.settings.AppTexts;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 * Controller des "Kategorien-Ersteller"-GUI. Erstellt neue Kategorien für die Kategorienauswahl der Hauptübersicht.
 */
public class CustomCategoryCreatorController {

    //region Attribute
    public TextField nameOfNewCategoryField;
    public Label statusIndicator;
    private Stage overlayStage;
    private ObservableList presentCategoryList;
    //endregion

    //region Methoden

    /**
     * Fügt neue, noch nicht vorhandene, Kategorie in die zur ChoiceBox
     * {@link BudgetOverviewController#categoryChoiceBoxForNewEntry} gehörende ObjektListe ein, basierend
     * auf den im Textfeld {@link CustomCategoryCreatorController#nameOfNewCategoryField} eingegebenen String.
     */
    @FXML
    private void addCategory() {
        if (!nameOfNewCategoryField.getText().contentEquals("")) {
            if (presentCategoryList.contains(nameOfNewCategoryField.getText())) {
                statusIndicator.setTextFill(Paint.valueOf(AppTexts.PAINT_RED));
                statusIndicator.setText(String.format(AppTexts.TXT_CATEGORY_ALREADY_EXISTS, nameOfNewCategoryField.getText()));
                return;
            }
            presentCategoryList.add(presentCategoryList.size() - 1, nameOfNewCategoryField.getText());
            overlayStage.close();
        }
    }

    @FXML
    private void exitCategoryScene() {
        overlayStage.close();
    }

    public void setPresentCategoryList(ObservableList presentCategoryList) {
        this.presentCategoryList = presentCategoryList;
    }

    public void setOverlayStage(Stage overlayStage) {
        this.overlayStage = overlayStage;
    }
    //endregion
}
