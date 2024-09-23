package de.mfrHaushaltsbuchFx.test;

import de.mfrHaushaltsbuchFx.logic.BookEntryHolder;
import de.mfrHaushaltsbuchFx.logic.ReoccurringBookingsHandler;
import de.mfrHaushaltsbuchFx.model.BookEntry;
import de.mfrHaushaltsbuchFx.model.EntryType;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

/**
 * Testklasse, die eine eigene View erzeugt, um diverse GUI-Objekte und deren Funktionen zu testen
 */
public class TestMain extends Application {
    public static void main(String[] args) {
        launch();
    }

    private static void testPieChart(VBox vBox) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Grapefruit", 13),
                new PieChart.Data("Oranges", 25),
                new PieChart.Data("Plums", 10),
                new PieChart.Data("Pears", 22),
                new PieChart.Data("Apples", 30));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Imported Fruits");

        vBox.getChildren().add(chart);
    }

    private static void testDatePicker(VBox vBox) {
        Label currentDateReadout = new Label("kein Datum ausgewählt");

        DatePicker datePicker = new DatePicker();

        datePicker.setValue(LocalDate.of(2000, 12, 1));

        datePicker.setOnAction(event -> {
            LocalDate date = datePicker.getValue();
            currentDateReadout.setText("Datum:" + date);
        });

        vBox.getChildren().add(datePicker);
        vBox.getChildren().add(currentDateReadout);

        LocalDate date = LocalDate.of(2023, 1, 31);
        System.out.println(date.plus(Period.ofMonths(1)));

        Date dateFormat = Date.valueOf(date);
        System.out.println(dateFormat);
        System.out.println(date);
    }

    @Override
    public void start(Stage stage) {
//        stage.setTitle("Test-Umgebung");
//        VBox vBox = new VBox();
//
//        testDatePicker(vBox);
//        testPieChart(vBox);
//        testTableView(vBox);
//
//        Scene scene = new Scene(vBox, 500, 500);
//        stage.setScene(scene);
//        stage.show();

//        testEntryTypeModel(); // TEST: OK
//        testModelHolderAndChangeListener(); // TEST: OK
//        testEnum(); // DERZEITIGE VARIANTE MIT EntryType-Objekt erscheint geeigneter, als ein enum zu sein

//        testDbManager(); // TEST: OK
//
//        testChangeListenerAndDb();


        BookEntry entryOne = new BookEntry(2, 200, LocalDate.of(2023, 1, 15),
                "Haushalt", "Waschmittel");

        entryOne.getEntryType().setInterval(3);

        ArrayList<LocalDate> datesList = ReoccurringBookingsHandler.getAllDatesOfFutureReoccurrences(entryOne, LocalDate.now());

        System.out.println(datesList);

        ArrayList<BookEntry> entries;
        entries = ReoccurringBookingsHandler.createEntriesBasedOnLocalDateList(entryOne, datesList);

        System.out.println(entries);

        BookEntryHolder.getInstance().getAccountingBook().add(entryOne);


        System.exit(0);
    }

    private void testChangeListenerAndDb() {
    }

    private void testDbManager() {
//        BookEntry entryOne = new BookEntry(0, 200, LocalDate.now(),
//                "Haushalt", "Waschmittel");
//
//        BookEntry entryTwo = new BookEntry(2, 50, LocalDate.now(),
//                "Lebensmittel", "Getränke");
//        entryTwo.getEntryType().setInterval(2);
//
//        BookEntry entryThree = new BookEntry(1, 2500, LocalDate.now(),
//                "Gehalt", "");
//
//        DbManager.getInstance().insertData(entryTwo);
//        DbManager.getInstance().insertData(entryThree);


        //ObservableList<BookEntry> accountingBook = BookEntryHolder.getInstance().getAccountingBook();
//        List<BookEntry> accountingBook = new ArrayList<>();
//        accountingBook.addAll(DbManager.getInstance().getAllBookEntries());

//        for (BookEntry entry : accountingBook) {
//            System.out.println(entry);
//        }

//        BookEntry entryOne = DbManager.getInstance().getBookEntryById(1);
//
//        entryOne.setAmount(33.22);
//
//        DbManager.getInstance().updateBookEntry(entryOne);
//
//
//        accountingBook.removeAll(accountingBook);
//        accountingBook.addAll(DbManager.getInstance().getAllBookEntries());

//        for (BookEntry entry : accountingBook) {
//            System.out.println(entry);
//        }

//        DbManager.getInstance().deleteBookEntry(entryOne);

    }

    private void testModelHolderAndChangeListener() {
        BookEntry entryOne = new BookEntry(0, 200, LocalDate.now(),
                "Haushalt", "Waschmittel");

        System.out.println(entryOne);


        BookEntry entryTwo = new BookEntry(1, 500, LocalDate.now(), "Gehalt",
                "-");

        System.out.println(entryTwo);

        ObservableList<BookEntry> accountingBook = BookEntryHolder.getInstance().getAccountingBook();
        accountingBook.add(entryOne);
        accountingBook.add(entryTwo);

        System.out.println(accountingBook);

        accountingBook.get(0).getEntryType().setAsReoccurringExpenses();
        accountingBook.get(0).getEntryType().setInterval(2);

        accountingBook.get(1).getEntryType().setAsReoccurringIncome();
        accountingBook.get(1).getEntryType().setInterval(1);

        System.out.println(accountingBook);


    }

    private void testEntryTypeModel() {
        EntryType expensesOne = new EntryType(0);
        System.out.println(expensesOne.typeProperty());

        expensesOne.setInterval(1);
//        expensesOne.getInterval().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                System.out.println("Interval changed");
//            }
//        });

        expensesOne.setInterval(2);
        expensesOne.setAsReoccurringExpenses();
    }

}