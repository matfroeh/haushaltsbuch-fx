package de.mfrHaushaltsbuchFx.test;

import de.mfrHaushaltsbuchFx.logic.BookEntryHolder;
import de.mfrHaushaltsbuchFx.model.BookEntry;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Testklasse, um die TableView-Klasse und ihre Verwendung zu testen.
 */

public class TestTableViewAccountingBook extends Application {

    public TableView<BookEntry> tableAccountingBook = new TableView<>();

    public ObservableList<BookEntry> accountingBook =
            BookEntryHolder.getInstance().getAccountingBook();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(450);
        stage.setHeight(500);

        final Label label = new Label("Book");
        label.setFont(new Font("Arial", 20));

        tableAccountingBook.setEditable(true);


        TableColumn colAmount = new TableColumn("Betrag");
        TableColumn colDate = new TableColumn("Datum");
        TableColumn colCategory = new TableColumn("Kategorie");
        TableColumn colDescription = new TableColumn("Beschreibung");


        colAmount.setCellValueFactory(new PropertyValueFactory<BookEntry, Double>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<BookEntry, LocalDate>("date"));
        colCategory.setCellValueFactory(new PropertyValueFactory<BookEntry, String>("category"));
        colDescription.setCellValueFactory(new PropertyValueFactory<BookEntry, String>("description"));

        tableAccountingBook.setItems(accountingBook);
        tableAccountingBook.getColumns().addAll(colDate, colAmount, colCategory, colDescription);


        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, tableAccountingBook);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
    }

}
