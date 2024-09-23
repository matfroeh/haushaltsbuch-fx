package de.mfrHaushaltsbuchFx.gui;

import de.mfrHaushaltsbuchFx.Main;
import de.mfrHaushaltsbuchFx.settings.AppTexts;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;



/**
 * Singleton-Klasse zum Wechsel der einzelnen Scenes bzw. der Stages.
 */
public class SceneManager {
    //region Konstanten
    //endregion

    //region Attribute
    private static SceneManager instance;
    private Stage mainStage;
    private Stage overlayStage;

    //endregion

    //region Konstruktoren
    private SceneManager() {
    }
    //endregion

    //region Methoden
    public static synchronized SceneManager getInstance() {
        if (instance == null) instance = new SceneManager();
        return instance;
    }

    public void setMainStage(Stage stage) {
        mainStage = stage;

        openBudgetOverviewScene();
    }

    /**
     * Öffnet die Hauptübersicht des GUI.
     */
    public void openBudgetOverviewScene() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("budgetOverview-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            mainStage.setTitle(AppTexts.SCENE_TITLE_OVERVIEW);

            switchScene(mainStage, scene);



        } catch (Exception e) {
            System.out.println(AppTexts.ERROR_LOADING_BUDGET_OVERVIEW_VIEW);
            e.printStackTrace();
        }
    }

    /**
     * Öffnet das "Kategorien-Ersteller"-Fenster. Hierüber kann eine neue Kategorie benannt werden, zur Verwendung
     * in der Hauptübersicht zum Anlegen neuer Buchungseinträge.
     * Die Hauptübersicht bleibt geöffnet.
     * @param presentChoiceBoxList die Liste der aktuell verwendeten Kategorien aus den GUI-Elementen (ChoiceBox)
     *                             aus der Hauptübersicht.
     */
    public void openCustomCategoryCreatorView(ObservableList presentChoiceBoxList) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("customCategoryCreator-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            CustomCategoryCreatorController categoryCreatorController = fxmlLoader.getController();

            overlayStage = new Stage();
            overlayStage.setTitle(AppTexts.SCENE_TITLE_CATEGORY_CREATOR);
            overlayStage.initOwner(mainStage);

            categoryCreatorController.setOverlayStage(overlayStage);
            categoryCreatorController.setPresentCategoryList(presentChoiceBoxList);

            switchSceneAndWait(overlayStage, scene);

        } catch (Exception e) {
            System.out.println(AppTexts.ERROR_LOADING_CUSTOM_CATEGORY_CREATOR_VIEW);
            e.printStackTrace();
        }
    }


    private void switchScene(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.show();
    }

    private void switchSceneAndWait(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.showAndWait();
    }

    //endregion
}
