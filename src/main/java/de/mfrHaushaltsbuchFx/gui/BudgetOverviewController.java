package de.mfrHaushaltsbuchFx.gui;

import de.mfrHaushaltsbuchFx.logic.BookEntryHolder;
import de.mfrHaushaltsbuchFx.logic.CustomCategoriesListHandler;
import de.mfrHaushaltsbuchFx.logic.ReoccurringBookingsHandler;
import de.mfrHaushaltsbuchFx.model.BookEntry;
import de.mfrHaushaltsbuchFx.model.EntryType;
import de.mfrHaushaltsbuchFx.settings.AppTexts;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

/**
 * Controller der Hauptübersicht "budgetOverview-view.fxml".
 * Funktionalitäten:
 * <ul>
 *     <li>Erstellen/Löschen/Bearbeiten von Haushaltsbucheinträgen (persistent).</li>
 *     <p>
 *     <li>Erstellen benutzerdefinierter Kategorien,
 *     die (sobald sie verwendet werden) persistent mitgespeichert werden.</li>
 *     </p>
 *     <li>Tabellenansicht mit an-/ausschaltbaren Filterkontrollen (Zeitraum, Eintragsart).</li>
 *     <li>Berechnung der Gesamtbilanz.</li>
 *     <li>Aufspaltung der Summe in die einzelnen Kategorien in einem Kuchendiagramm über einen gewählten
 *     Zeitraum. Neudefinierte Kategorie werden automatisch in das Diagramm mitaufgenommen, sobald sie
 *     in Buchungseinträgen verwendet werden.</li>
 *     <li>Berechnung der Gesamtsumme pro Kategorie (mit-/ohne Filter). Wird ein neuer
 *     Buchungseintrag mit einer neudefinierten Kategorie erstellt, ist diese automatisch zur
 *     Berechnung verfügbar.</li>
 *     <li>Ist ein Eintrag der Art "wiederkehrend" in der Tabellenansicht markiert,
 *      können über die Schaltfläche "Zukünftige Buchungen [...] einfügen" alle (bis zum gewählten
 *      End-Datum) anfallenden Buchungen in die Daten eingefügt werden.</li>
 *      <p>
 *     <li>Alle Anzeigen/Darstellungen spiegeln stets die aktuellen in der Tabellenansicht herrschenden
 *     Verhältnisse wieder. Kein manuelles Aktualisieren notwendig.</li>
 *     </p>
 * </ul>
 */
public class BudgetOverviewController {

    //region Konstanten
    public static final LocalDate LOCAL_DATE_ARBITRARY_START_DATE = LocalDate.of(2000, 1, 1);
    public static final LocalDate LOCAL_DATE_ARBITRARY_END_DATE = LocalDate.of(2000, 12, 31);
    public static final LocalDate DEFAULT_STARTING_PERIOD_FOR_PERIOD_FILTER = LocalDate.of(2023, 9, 1);
    public static final LocalDate DATE_OF_TODAY = LocalDate.now();
    public static final int INDEX_INCOME_OF_MONTH = 0;
    public static final int INDEX_EXPENSES_OF_MONTH = 1;
    //endregion

    //region Attribute
    @FXML
    private BarChart barChartMonthlyBudget;
    @FXML
    private TextField amountField;
    @FXML
    private TextField searchField;
    @FXML
    private TextField descriptionField;
    @FXML
    private ChoiceBox categoryChoiceBoxForNewEntry;
    @FXML
    private PieChart pieChart;
    @FXML
    private TableView<BookEntry> tableAccountingBook;
    @FXML
    private TableColumn colAmount;
    @FXML
    private TableColumn colDate;
    @FXML
    private TableColumn colDescription;
    @FXML
    private TableColumn colCategory;
    @FXML
    private TableColumn colReoccurrence;
    @FXML
    private RadioButton rdButtonExpense;
    @FXML
    private RadioButton rdButtonIncome;
    @FXML
    private CheckBox checkBoxIsNewEntryReoccurring;
    @FXML
    private Label statusNotifier;
    @FXML
    private Label labelIntervalField;
    @FXML
    private TextField intervalField;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnCancelEdit;
    @FXML
    private Button btnSaveEditedEntry;
    @FXML
    private TextArea summaryOutputTxtArea;
    @FXML
    private CheckBox checkBoxShowExpensesOnly;
    @FXML
    private CheckBox checkBoxReoccurringOnly;
    @FXML
    private DatePicker dateStartPicker;
    @FXML
    private DatePicker datePeriodEndPicker;
    @FXML
    private ComboBox comboBoxFilterByCategories;
    @FXML
    private CheckBox checkBoxIsFilterActive;
    @FXML
    private HBox containerFilterControls;
    @FXML
    private TextArea outputTextAreaSummaryCategory;
    @FXML
    private Button btnAddFutureBookings;
    @FXML
    private DatePicker datePickerEndDateOfReoccurrence;
    @FXML
    private Label labelEndDateOfReoccurrence;
    @FXML
    private DatePicker datePicker;

    private LocalDate date;
    private BookEntry selectedEntryInTableView;
    private ObservableList<BookEntry> accountingBook;
    //endregion

    //region Methoden
    @FXML
    private void initialize() {

        //DatePicker initialisieren
        setUpDatePicker();

        //AccountingBook aus DB holen und in den Holder laden
        accountingBook = BookEntryHolder.getInstance().getAccountingBook();

        //TableView einrichten
        setUpTableViewForAccountingBook(accountingBook);

        //FilteredList Wrapper
        FilteredList<BookEntry> filteredAccBook = new FilteredList<>(accountingBook);

        //Listener für Filterkontrollen und Filterkriterien einrichten
        setUpListenerForFilterControls(filteredAccBook);

        //Zeitraumwähler-DatePicker einstellen
        setUpDatePeriodToBeSelectedPicker();

        //FilteredList in SortedList, da FilteredList nicht sortierbar ist über Sortierwähler
        SortedList<BookEntry> sortedAccBook = new SortedList<>(filteredAccBook);

        sortedAccBook.comparatorProperty().bind(tableAccountingBook.comparatorProperty());

        //Liste in TableView laden
        tableAccountingBook.setItems(sortedAccBook);

        //Textfelder einrichten:
        setUpTextFields();

        //CategoryChoiceBox einrichten
        setUpCategoryChoiceBox();

        //KategorienFilter-ComboBox einrichten
        setUpFilterByCategoriesComboBox();

        //Ausgabetextbereich einrichten
        printBudgetSummariesInTextArea();

        //PieChart einrichten
        setUpPieChart();

//        //Balkendiagramm einrichten
//        setUpBarChart();

        //Listener für die TableView an sich einrichten
        setUpListChangeListenerForTableView();

        //Suchleiste einrichten
        setUpSearchField(filteredAccBook);

    }

    /**
     * Registriert Veränderungen in der Anzeige-an-sich, damit die darauf basierenden Ausgaben (PieChart, Betragssumme in gewählter Kategorie)
     * stets auch die angezeigten Elemente widerspiegeln.
     */
    private void setUpListChangeListenerForTableView() {
        tableAccountingBook.getItems().addListener(new ListChangeListener<BookEntry>() {
            @Override
            public void onChanged(Change<? extends BookEntry> c) {

                printBudgetSummariesInTextArea();
                setUpPieChart();
//                setUpBarChart();
            }
        });
    }

    /**
     * Gibt die monatlichen Einnahmen MINUS Ausgaben für das aktuelle Jahr und die Gesamtbilanz über die {@link #summaryOutputTxtArea} aus.
     * @param monthlyBalanceList die (über {@link #getListOfMonthlyBalances(ObservableList, int)}) errechnete Liste der Ausgaben/Einnahmen für jeden Monat
     */
    private void printMonthlyBalance(ArrayList<double[]> monthlyBalanceList) {

        String txtMonthlyBalanceHeader = String.format(AppTexts.TXT_OUTPUT_MONTHLY_BALANCE,
                datePicker.getValue().getYear());

        StringBuilder txtMonthlyBalanceBody = new StringBuilder();
        double sumOfYear = 0.0;

        for (int i = 0; i < AppTexts.MONTHS.length; i++) {
            txtMonthlyBalanceBody.append(String.format(AppTexts.TEMPLATE_MONTHLY_BALANCE,
                    AppTexts.MONTHS[i], monthlyBalanceList.get(i)[INDEX_INCOME_OF_MONTH], monthlyBalanceList.get(i)[INDEX_EXPENSES_OF_MONTH],
                    monthlyBalanceList.get(i)[INDEX_INCOME_OF_MONTH] - monthlyBalanceList.get(i)[INDEX_EXPENSES_OF_MONTH]));

            sumOfYear += (monthlyBalanceList.get(i)[INDEX_INCOME_OF_MONTH] - monthlyBalanceList.get(i)[INDEX_EXPENSES_OF_MONTH]);
        }

        txtMonthlyBalanceBody.append(String.format(AppTexts.TEMPLATE_BALANCE_OF_YEAR, sumOfYear));

        summaryOutputTxtArea.setText(txtMonthlyBalanceHeader + txtMonthlyBalanceBody);
    }

    /**
     * Erzeugt eine ArrayList mit 12 Elementen, wobei jedes davon einen Monat des gewählten Jahres repräsentiert, wobei jedes Element ein 2D-Double-Array ist.
     * Im Double-Array steht dabei Index = 0 für die Gesamteinnahmen und Index = 1 für die Gesamtausgaben.
     * @param accountingBook das Haushaltsbuch mit allen BookEntry-Einträgen
     * @param year das gewählte Jahr, nachdem das accountingBook gefiltert werden soll
     * @return die entsprechende ArrayList mit 12 double[2] Elementen
     */
    private ArrayList<double[]> getListOfMonthlyBalances(ObservableList<BookEntry> accountingBook, int year) {

        ArrayList<double[]> monthlyBalanceList = new ArrayList<>();

        FilteredList<BookEntry> filteredList = new FilteredList<>(accountingBook);

        for (int i = 1; i <= 12; i++) {

            int month = i;

            filteredList.setPredicate(entry -> entry.getDate().getMonthValue() == month
                    && entry.getDate().getYear() == year);

            double[] balanceOfMonth = new double[2];
            double incomeOfMonth = getAllIncomeFromList(filteredList);
            double expensesOfMonth = getAllExpensesFromList(filteredList);
            balanceOfMonth[INDEX_INCOME_OF_MONTH] = incomeOfMonth;
            balanceOfMonth[INDEX_EXPENSES_OF_MONTH] = expensesOfMonth;
            monthlyBalanceList.add(balanceOfMonth);
        }

        return monthlyBalanceList;

    }

    /**
     * Richtet das Suchfeld ein. Die TableView zeigt synchron zur Eingabe die entsprechenden gefundenen Einträge an. Bei der Sucheingabe werden alle aktiven Filter automatisch deaktiviert.
     * @param filteredList
     */
    private void setUpSearchField(FilteredList<BookEntry> filteredList) {

        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                checkBoxIsFilterActive.setSelected(false); //alle Einträge sollen durchsucht werden; keine sonstige Filterung.

                if (searchField.getText().contentEquals("")) return;

                filteredList.setPredicate(entry ->
                        entry.toString().toLowerCase().contains(searchField.getText().toLowerCase()));

            }
        });
    }


    /**
     * Richtet die Auswahlbox {@link #comboBoxFilterByCategories} ein, die dem Nutzer ermöglicht, die einzelnen Betragssummen
     * der in der Tabelle angezeigten Einträge der jeweiligen Kategorie über das Textfeld {@link #outputTextAreaSummaryCategory}
     * ausgeben zu lassen.
     */
    private void setUpFilterByCategoriesComboBox() {
        // Die Filterbox soll natürlich auf das gleiche Listenobjekt wie die Kategorienauswahl, die beim Eintragen
        // neuer Buchungen verwendet wird, zugreifen, damit neu angelegte Kategorien auch dort angezeigt werden.
        // Muss entsprechend gefiltert werden, damit der Eintrag "<Neue Kategorie anlegen>" nicht darin vorkommt.

        ObservableList<String> listForFilteringCategoriesBox =
                new FilteredList<String>(categoryChoiceBoxForNewEntry.getItems(), item -> {
                    return !item.toString().contentEquals(AppTexts.NAME_OF_CHOICE_BOX_FIELD_CREATE_NEW_CATEGORY);
                });

        comboBoxFilterByCategories.setItems(listForFilteringCategoriesBox);

        comboBoxFilterByCategories.getSelectionModel().selectFirst();

        comboBoxFilterByCategories.getSelectionModel().selectedIndexProperty().
                addListener((observable, oldValue, newValue) -> printBudgetSummariesInTextArea());
    }

    /**
     * Berechnet die Gesamtsumme in der als String übergebenen Kategorie der derzeit in der Tabelle angezeigten Einträge.
     * @param categoryName der Name der Kategorie als String
     * @return die Gesamtsumme in der Kategorie als double
     */
    private double calculateSumOfEntriesInCategory(String categoryName) {
        double result = 0.0;

        for (BookEntry entry : tableAccountingBook.getItems()) {
            if (entry.getCategory().contentEquals(categoryName))
                result += entry.getAmount();
        }

        return result;
    }

    private void setUpDatePeriodToBeSelectedPicker() {
        dateStartPicker.setValue(DEFAULT_STARTING_PERIOD_FOR_PERIOD_FILTER);
        datePeriodEndPicker.setValue(DATE_OF_TODAY);
    }

    /**
     * Richtet die Listener für die Filterkontrollen ein. Verwendet die Methode {@link #applyFilterPredicates(FilteredList)}.
     * @param filteredAccBook
     */
    private void setUpListenerForFilterControls(FilteredList<BookEntry> filteredAccBook) {

        dateStartPicker.valueProperty().addListener((observable, oldValue, newValue) ->
                applyFilterPredicates(filteredAccBook));


        datePeriodEndPicker.valueProperty().addListener((observable, oldValue, newValue) ->
                applyFilterPredicates(filteredAccBook));


        checkBoxShowExpensesOnly.selectedProperty().addListener(
                (observable, oldValue, newValue) ->
                        applyFilterPredicates(filteredAccBook));


        checkBoxReoccurringOnly.selectedProperty().addListener(
                (observable, oldValue, newValue) ->
                        applyFilterPredicates(filteredAccBook));


        checkBoxIsFilterActive.selectedProperty().addListener((observable, oldValue, newValue) ->

                {
                    applyFilterPredicates(filteredAccBook);

                    if (checkBoxIsFilterActive.isSelected()) {
                        statusNotifier.setTextFill(Paint.valueOf(AppTexts.PAINT_BLUE));
                        statusNotifier.setText(AppTexts.TXT_FILTER_ON);
                        searchField.setText("");
                        checkBoxIsFilterActive.setSelected(true); // unschön, aber setText("") setzt in der Logik die checkBox disabled
                    } else {
                        statusNotifier.setTextFill(Paint.valueOf(AppTexts.PAINT_GREEN));
                        statusNotifier.setText(AppTexts.FILTER_OFF);
                    }
                }

        );
    }

    /**
     * Filteraussagen, die durch die Wahl der entsprechenden Filterkontrollen auf die übergebene filteredList angewandt werden.
     * Die checkBoxIsFilterActive ermöglicht dabei das an-/ausschalten aller Filter, d.h. bei ausgeschalteten Filter werden alle Einträge des Holders angezeigt.
     * Bei angeschaltetem Filter wird zunächst gefiltert, ob ein Eintrag innerhalb der gewählten Zeitperiode sich befindet.
     * Danach wird überprüft, ob die zwei weiteren Filter nach Eintragstyp aktiv/inaktiv sind und ob ein Eintrag das entsprechend gewählte Kriterium erfüllt.
     * @param filteredList
     */
    private void applyFilterPredicates(FilteredList<BookEntry> filteredList) {
        filteredList.setPredicate(item -> {

            if (checkBoxIsFilterActive.isSelected()) {

                containerFilterControls.setDisable(false);

                if ((item.getDate().isAfter(dateStartPicker.getValue()) || item.getDate().isEqual(dateStartPicker.getValue()))
                        && (item.getDate().isBefore(datePeriodEndPicker.getValue()) || item.getDate().isEqual(datePeriodEndPicker.getValue()))) {

                    if (checkBoxShowExpensesOnly.isSelected() && checkBoxReoccurringOnly.isSelected()) {

                        return (item.getEntryType().isOfTypeExpenses() && item.getEntryType().isOfTypeReoccurring());

                    } else if (checkBoxShowExpensesOnly.isSelected() && !checkBoxReoccurringOnly.isSelected()) {
                        return item.getEntryType().isOfTypeExpenses();

                    } else if (!checkBoxShowExpensesOnly.isSelected() && checkBoxReoccurringOnly.isSelected()) {
                        return item.getEntryType().isOfTypeReoccurring();
                    } else {
                        return true; // alle Items innerhalb des Zeitraums
                    }

                } else {
                    return false; // Item ist nicht innerhalb des gewählten Zeitraums
                }
            } else {
                //Es soll ersichtlich sein, dass die Filterkontrollen ausgeschaltet sind
                containerFilterControls.setDisable(true);

                return true; // Alle items in DB-Tabelle
            }

        });
    }

    /**
     * Richtet die Auswahlbox für das Auswählen einer Kategorie beim Erstellen neuer Buchungseinträge ein. "Benutzerdefinierte Kategorien" werden über
     * die in der Klasse {@link CustomCategoriesListHandler} beschriebene Methode geladen.
     * Ein Eintrag mit dem Namen "Neue Kategorie anlegen" wird in die Box hinzugefügt, der bei Auswahl das "CustomCategoryCreator"-Fenster öffnet.
     * */
    private void setUpCategoryChoiceBox() {

        ObservableList preDefinedCategories = categoryChoiceBoxForNewEntry.getItems();

        //  Liste der in der DB auffindbaren Kategorien holen
        //  (exklusive der bereits in der fxml vordefinierten Kategorien)
        ObservableList<String> customCategoryList =
                CustomCategoriesListHandler.getCustomCategoriesList(preDefinedCategories);

        //  Benutzerdefinierte Kategorien aus der DB hinzufügen und sie zwischen vorletztem Element
        //  und "Neue Kategorie anlegen" einfügen
        categoryChoiceBoxForNewEntry.getItems().addAll(preDefinedCategories.size() - 1, customCategoryList);

        //  Alphabetisch aufsteigend sortieren
        //TODO: nach Häufigkeit der Verwendung sortieren (z.B.: Methode getCustomCategories im EntryHolder liefert Array mit Häufigkeit in DB zurück etc.)
        categoryChoiceBoxForNewEntry.getItems().sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        //  "Neue Kategorie anlegen"-Eintrag an das Ende der Liste setzen
        categoryChoiceBoxForNewEntry.getItems().add(categoryChoiceBoxForNewEntry.getItems().size(), AppTexts.NAME_OF_CHOICE_BOX_FIELD_CREATE_NEW_CATEGORY);

        // Wenn "Neue Kategorie erstellen" ausgewählt wird:
        categoryChoiceBoxForNewEntry.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (categoryChoiceBoxForNewEntry.getSelectionModel().isSelected(categoryChoiceBoxForNewEntry.getItems().size() - 1)) {

                    //Soll nach Aufruf wieder in Ursprungszustand kehren
                    categoryChoiceBoxForNewEntry.getSelectionModel().select(-1);

                    SceneManager.getInstance().openCustomCategoryCreatorView(categoryChoiceBoxForNewEntry.getItems());

                }
            }
        });

        // Listener für ChoiceBoxListe einrichten, damit direkt die neu erstellte Kategorie ausgewählt wird
        categoryChoiceBoxForNewEntry.getItems().addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change c) {

                while (c.next()) {

                    if (c.wasAdded()) {
                        Object addedObject = c.getAddedSubList().get(0);
                        categoryChoiceBoxForNewEntry.getSelectionModel().select(addedObject);
                    }
                }
            }
        });
    }

    /** Erstellt eine PieChart.Data-ArrayList, wobei jedes Element die Gesamtsumme in einer jeweiligen Kategorie repräsentiert.
     *
     * @return
     */
    private ArrayList<PieChart.Data> getPieChartDataAllCategories() {

        ArrayList<PieChart.Data> pieChartDataList = new ArrayList<>();

        for (Object categoryName : comboBoxFilterByCategories.getItems()) {

            PieChart.Data currentData =
                    new PieChart.Data(categoryName.toString(),
                            calculateSumOfEntriesInCategory(categoryName.toString()));

            pieChartDataList.add(currentData);
        }


        return pieChartDataList;
    }

    private void setUpPieChart() {

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                getPieChartDataAllCategories()
        );

        pieChart.setData(pieChartData);
        pieChart.setLabelsVisible(false);
    }


    private void setUpTextFields() {

        //  Betragsfeld: nur positive double
        amountField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter() {

            @Override
            public Double fromString(String value) {
                // If the specified value is null or zero-length, return null
                if (value == null) {
                    return null;
                }

                value = value.trim();

                if (value.isEmpty()) {
                    return null;
                }
                return Math.abs(Double.parseDouble(value));
            }
        }
        ));

        //  Intervallfeld: nur positive Integer zulässig
        intervalField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter() {

            @Override
            public Integer fromString(String value) {
                if (value == null) {
                    return null;
                }

                value = value.trim();

                if (value.isEmpty()) {
                    return null;
                }
                return Integer.parseUnsignedInt(value);
            }
        }));
    }

    private void setUpDatePicker() {
        //Wichtig, dass alle DatePicker ganz am Anfang initialisiert werden, da sonst nullpointer bei Einrichtung d. Filter
        date = DATE_OF_TODAY;
        datePicker.setValue(date);
        datePicker.setOnAction(event -> date = datePicker.getValue());
        dateStartPicker.setValue(LOCAL_DATE_ARBITRARY_START_DATE);
        datePeriodEndPicker.setValue(LOCAL_DATE_ARBITRARY_END_DATE);
    }

    private void setUpTableViewForAccountingBook(ObservableList<BookEntry> accountingBook) {
        // Spalten formatieren
        // Datum in lokales Format bringen
        colDate.setCellFactory(column -> {
            TableCell<BookEntry, LocalDate> cell = new TableCell<BookEntry, LocalDate>() {

                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setText(null);
                    } else {
                        this.setText(item.format(DateTimeFormatter.ofPattern(AppTexts.TEMPLATE_DATE_PATTERN_FOR_TABLE)));
                    }
                }
            };
            return cell;
        });

        // Beträge: stets genau 2 gültige Kommastellen anzeigen
        colAmount.setCellFactory(column -> {
            TableCell<BookEntry, Double> cell = new TableCell<BookEntry, Double>() {

                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setText(null);

                    } else {
                        this.setText(String.format(AppTexts.TEMPLATE_TABLE_FORMAT_FOR_DOUBLES, item));
                    }
                }
            };
            return cell;
        });

        // Buchungstyp: je nach Ausgabe/Einnahme Zellenhintergrund der jew. Zelle anpassen.
        colReoccurrence.setCellFactory(column -> {
            TableCell<BookEntry, EntryType> cell = new TableCell<BookEntry, EntryType>() {

                @Override
                protected void updateItem(EntryType item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null) {
                        setText(null);
                        setStyle("");

                    } else if (item.isOfTypeExpenses()) {
                        setText(item.toString());
                        setStyle(AppTexts.FX_BACKGROUND_COLOR_SALMON);

                    } else if (item.isOfTypeIncome()) {
                        setText(item.toString());
                        setStyle(AppTexts.FX_BACKGROUND_COLOR_PALEGREEN);
                    }
                }
            };
            return cell;
        });


        // Spalten dem jeweiligen Attribut der Modellklasse BookEntry zuweisen
        colAmount.setCellValueFactory(new PropertyValueFactory<BookEntry, Double>(AppTexts.TXT_AMOUNT));
        colDate.setCellValueFactory(new PropertyValueFactory<BookEntry, LocalDate>(AppTexts.TXT_DATE));
        colCategory.setCellValueFactory(new PropertyValueFactory<BookEntry, String>(AppTexts.TXT_CATEGORY));
        colDescription.setCellValueFactory(new PropertyValueFactory<BookEntry, String>(AppTexts.TXT_DESCRIPTION));
        colReoccurrence.setCellValueFactory(new PropertyValueFactory<BookEntry, EntryType>(AppTexts.TXT_ENTRY_TYPE));

        //TODO: Listener extra für EntryType-Spalte


        // Absteigend nach Datum sortieren (
        //sortByDateDescending(accountingBook);
        colDate.setSortType(TableColumn.SortType.DESCENDING);
        tableAccountingBook.getSortOrder().add(colDate);


        // Listener für Auswahl der Einträge einrichten
        tableAccountingBook.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldSelection, newSelection) -> {

                    if (newSelection != null) {
                        //Buttons nur aktiv falls Auswahl getroffen
                        btnDelete.setDisable(false);
                        btnEdit.setDisable(false);
                        //aktive Auswahl in Variable speichern
                        selectedEntryInTableView = newSelection;

                        if (newSelection.getEntryType().isOfTypeReoccurring()) {
                            btnAddFutureBookings.setDisable(false);
                            datePickerEndDateOfReoccurrence.setDisable(false);
                            datePickerEndDateOfReoccurrence.setValue(newSelection.getDate());
                            labelEndDateOfReoccurrence.setDisable(false);
                        } else {
                            btnAddFutureBookings.setDisable(true);
                            datePickerEndDateOfReoccurrence.setDisable(true);
                            datePickerEndDateOfReoccurrence.setValue(null);
                            labelEndDateOfReoccurrence.setDisable(true);
                        }

                    }
                    //TODO: Wie kommt ans Event "no selection"??
                });
    }

    private void sortByDateDescending(ObservableList<BookEntry> accountingBook) {
        tableAccountingBook.setItems(accountingBook);
        FXCollections.sort(tableAccountingBook.getItems(), new Comparator<BookEntry>() {
            @Override
            public int compare(BookEntry o1, BookEntry o2) {
                return -o1.getDate().compareTo(o2.getDate());
            }
        });
    }

    private void printBudgetSummariesInTextArea() {

        ArrayList<double[]> monthlyBalanceList = getListOfMonthlyBalances(accountingBook, datePicker.getValue().getYear());

        printMonthlyBalance(monthlyBalanceList);

        printSumByCategory();
    }

    /**
     * Gibt die Gesamtsumme in einer Kategorie bei Auswahl dieser in der ComboBox über das Textfeld aus.
     */
    private void printSumByCategory() {
        outputTextAreaSummaryCategory.setText(String.format(AppTexts.TEMPLATE_PRINT_SUMMARY_BY_CATEGORIES,
                comboBoxFilterByCategories.getSelectionModel().getSelectedItem().toString(),
                calculateSumOfEntriesInCategory(comboBoxFilterByCategories.getSelectionModel().getSelectedItem().toString())
        ));
    }

    private double getAllExpensesOfDisplayedEntries() {
        double sumExpenses = 0.0;
        for (BookEntry entry : tableAccountingBook.getItems()) {
            if (entry.getEntryType().isOfTypeExpenses()) {
                sumExpenses += entry.getAmount();
            }
        }
        return sumExpenses;
    }

    private double getAllIncomeOfDisplayedEntries() {
        double sumIncome = 0.0;
        for (BookEntry entry : tableAccountingBook.getItems()) {
            if (entry.getEntryType().isOfTypeIncome()) {
                sumIncome += entry.getAmount();
            }
        }
        return sumIncome;
    }

    private double getAllIncomeFromList(ObservableList<BookEntry> list) {
        double sumIncome = 0.0;
        for (BookEntry entry : list) {
            if (entry.getEntryType().isOfTypeIncome()) {
                sumIncome += entry.getAmount();
            }
        }
        return sumIncome;
    }

    private double getAllExpensesFromList(ObservableList<BookEntry> list) {
        double sumExpenses = 0.0;
        for (BookEntry entry : list) {
            if (entry.getEntryType().isOfTypeExpenses()) {
                sumExpenses += entry.getAmount();
            }
        }
        return sumExpenses;
    }

    /**
     * Fügt einen neuen Eintrag in die ObservableList accountingBook des BookEntryHolders ein.
     */
    @FXML
    private void addEntry() {
        if (allRequiredFieldsFilled()) {
            int typeIndex = -1;
            int interval = 0;

            //Eintragtypus abfragen
            if (rdButtonExpense.isSelected() && !checkBoxIsNewEntryReoccurring.isSelected()) {
                typeIndex = EntryType.BOOKING_TYPE_INDEX_OF_EXPENSES;

            } else if (rdButtonIncome.isSelected() && !checkBoxIsNewEntryReoccurring.isSelected()) {
                typeIndex = EntryType.BOOKING_TYPE_INDEX_OF_INCOME;

            } else if (rdButtonExpense.isSelected() && checkBoxIsNewEntryReoccurring.isSelected()) {
                typeIndex = EntryType.BOOKING_TYPE_INDEX_OF_REOCCURRING_EXPENSES;
                interval = Integer.parseInt(intervalField.getText());

            } else if (rdButtonIncome.isSelected() && checkBoxIsNewEntryReoccurring.isSelected()) {
                typeIndex = EntryType.BOOKING_TYPE_INDEX_OF_REOCCURRING_INCOME;
                interval = Integer.parseInt(intervalField.getText());

            }

            //neuen Eintrag anlegen
            EntryType entryType = new EntryType(typeIndex, interval);
            BookEntry newBookEntry = new BookEntry(
                    entryType,
                    Double.parseDouble(amountField.getText()),
                    date,
                    categoryChoiceBoxForNewEntry.getSelectionModel().getSelectedItem().toString(),
                    descriptionField.getText()
            );

            //in DB speichern
            BookEntryHolder.getInstance().getAccountingBook().add(newBookEntry);

            statusNotifier.setTextFill(Paint.valueOf(AppTexts.PAINT_GREEN));
            statusNotifier.setText(AppTexts.TXT_ENTRY_SAVED);
            resetInputFields();

        } else {
            statusNotifier.setTextFill(Paint.valueOf(AppTexts.PAINT_RED));
            statusNotifier.setText(AppTexts.ERROR_REQUIRED_FIELDS_NOT_FILLED_BEFORE_SAVING);
        }
    }

    private void resetInputFields() {
        intervalField.setText("");
        amountField.setText("");
        descriptionField.setText("");
    }

    /**
     * Prüft, ob alle für das Anlegen eines neuen Eintrags erforderlichen Angaben gemacht wurden.
     * @return
     */
    private boolean allRequiredFieldsFilled() {
        if (!checkBoxIsNewEntryReoccurring.isSelected()) {
            return categoryChoiceBoxForNewEntry.getSelectionModel().getSelectedIndex() != -1 &&
                    !amountField.getText().contentEquals("") && datePicker.getValue() != null;

        } else if (checkBoxIsNewEntryReoccurring.isSelected()) {
            return categoryChoiceBoxForNewEntry.getSelectionModel().getSelectedIndex() != -1
                    && !amountField.getText().contentEquals("") && datePicker.getValue() != null
                    && !intervalField.getText().contentEquals("");
        }
        return false;
    }

    @FXML
    public void handleReoccurrenceInputFields() {
        if (checkBoxIsNewEntryReoccurring.isSelected()) {
            labelIntervalField.setDisable(false);
            intervalField.setDisable(false);
        } else {
            labelIntervalField.setDisable(true);
            intervalField.setDisable(true);
            intervalField.setText("");
        }
    }

    /**
     * Löscht den markierten Eintrag {@link #selectedEntryInTableView} aus der ObservableList accountingBook des BookEntryHolders.
     */
    @FXML
    public void deleteEntry() {
        Alert conveyAlert = new Alert(Alert.AlertType.CONFIRMATION);
        conveyAlert.setHeaderText(String.format(AppTexts.QUERY_DELETE_ENTRY));
        conveyAlert.setContentText(selectedEntryInTableView.toString());

        Optional<ButtonType> optional = conveyAlert.showAndWait();

        if (optional.isPresent() && optional.get() == ButtonType.OK) {

            BookEntryHolder.getInstance().getAccountingBook().remove(selectedEntryInTableView);
            statusNotifier.setTextFill(Paint.valueOf(AppTexts.PAINT_GREEN));
            statusNotifier.setText(AppTexts.TXT_DELETION_COMPLETED);
        }
        btnDelete.setDisable(true);
        btnEdit.setDisable(true);
        selectedEntryInTableView = null;
        tableAccountingBook.getSelectionModel().clearSelection();
    }

    /**
     * Bearbeitet den markierten Eintrag {@link #selectedEntryInTableView}, aktiviert den "Bearbeitungsmodus", der die
     * Attribute des markierten Eintrags an die entsprechenden Text-/Auswahlfelder gibt und speichert die Änderungen bei Wahl
     * der Schaltfläche btnSaveEditedEntry. Bricht den Modus ab und verwirft alle Änderungen, bei Wahl der Schaltfläche btnCancelEdit.
     */
    @FXML
    public void editEntry() {
        resetInputFields();
        statusNotifier.setTextFill(Paint.valueOf(AppTexts.PAINT_RED));
        statusNotifier.setText(AppTexts.TXT_EDIT_MODE_ACTIVE);
        tableAccountingBook.setDisable(true);
        btnDelete.setDisable(true);
        btnEdit.setDisable(true);
        btnAdd.setDisable(true);

        btnCancelEdit.setDisable(false);
        btnSaveEditedEntry.setDisable(false);


        btnCancelEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                exitEditModeAndResetInterfaceToDefault();
                statusNotifier.setText("");
            }
        });

        //TODO: überflüssige Referenz auf Eintrag
        BookEntry entryToBeEdited = selectedEntryInTableView;

        amountField.setText(String.valueOf(entryToBeEdited.getAmount()));
        descriptionField.setText(entryToBeEdited.getDescription());
        String categoryAsString = entryToBeEdited.getCategory();
        categoryChoiceBoxForNewEntry.getSelectionModel().select(categoryAsString);
        datePicker.setValue(entryToBeEdited.getDate());

        EntryType entryTypeToBeEdited = entryToBeEdited.getEntryType();
        int bookingTypeIndex = EntryType.getIndexOfBookingTypeByName(entryTypeToBeEdited.getTypeName());


        if (bookingTypeIndex == EntryType.BOOKING_TYPE_INDEX_OF_EXPENSES || bookingTypeIndex == EntryType.BOOKING_TYPE_INDEX_OF_INCOME) {

            checkBoxIsNewEntryReoccurring.setSelected(false);
            handleReoccurrenceInputFields();

            if (bookingTypeIndex == EntryType.BOOKING_TYPE_INDEX_OF_EXPENSES) rdButtonExpense.setSelected(true);
            else rdButtonIncome.setSelected(true);

        } else if (bookingTypeIndex == EntryType.BOOKING_TYPE_INDEX_OF_REOCCURRING_EXPENSES
                || bookingTypeIndex == EntryType.BOOKING_TYPE_INDEX_OF_REOCCURRING_INCOME) {

            checkBoxIsNewEntryReoccurring.setSelected(true);
            handleReoccurrenceInputFields();
            intervalField.setText(String.valueOf(entryTypeToBeEdited.getInterval()));

            if (bookingTypeIndex == EntryType.BOOKING_TYPE_INDEX_OF_REOCCURRING_EXPENSES)
                rdButtonExpense.setSelected(true);
            else rdButtonIncome.setSelected(true);
        }

        // Änderungen speichern
        btnSaveEditedEntry.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (allRequiredFieldsFilled()) {

                    //Eintragtypus abfragen
                    if (rdButtonExpense.isSelected() && !checkBoxIsNewEntryReoccurring.isSelected()) {
                        entryToBeEdited.getEntryType().setAsExpenses();

                    } else if (rdButtonIncome.isSelected() && !checkBoxIsNewEntryReoccurring.isSelected()) {
                        entryToBeEdited.getEntryType().setAsIncome();

                    } else if (rdButtonExpense.isSelected() && checkBoxIsNewEntryReoccurring.isSelected()) {
                        entryToBeEdited.getEntryType().setAsReoccurringExpenses();
                        entryToBeEdited.getEntryType().setInterval(Integer.parseInt(intervalField.getText()));

                    } else if (rdButtonIncome.isSelected() && checkBoxIsNewEntryReoccurring.isSelected()) {
                        entryToBeEdited.getEntryType().setAsReoccurringIncome();
                        entryToBeEdited.getEntryType().setInterval(Integer.parseInt(intervalField.getText()));
                    }

                    entryToBeEdited.setAmount(Double.parseDouble(amountField.getText()));
                    entryToBeEdited.setDescription(descriptionField.getText());
                    entryToBeEdited.setCategory(categoryChoiceBoxForNewEntry.getSelectionModel().getSelectedItem().toString());
                    entryToBeEdited.setDate(datePicker.getValue());

                    statusNotifier.setTextFill(Paint.valueOf(AppTexts.PAINT_GREEN));
                    statusNotifier.setText(AppTexts.TXT_ENTRY_CHANGED_AND_SAVED);

                    //Leider unschön, aber die "Buchungsart"-Spalte wird sonst nicht geupdated, da an EntryType gebunden.
                    //Der EntryType selbst ist jedoch kein ObservableValue, nur seine Attribute. D.h. die Tabelle
                    //merkt vermutlich(!) nicht, dass der EntryType geändert wurde (die Liste selbst natürlich schon,
                    //da die Attribute in der ObservableList auch angegeben sind, d.h. speichern in DB funktioniert).
                    //TODO: die EntryType-Klasse an sich als observable implementieren?
                    tableAccountingBook.refresh();

                    exitEditModeAndResetInterfaceToDefault();

                } else {
                    statusNotifier.setTextFill(Paint.valueOf(AppTexts.PAINT_RED));
                    statusNotifier.setText(AppTexts.ERROR_REQUIRED_FIELDS_NOT_FILLED_BEFORE_SAVING);
                }

            }
        });
    }

    /**
     * Verlässt den Bearbeitungsmodus und setzt alle damit verbundenen Elemente auf den Ursprungszustand zurück.
     */
    private void exitEditModeAndResetInterfaceToDefault() {
        resetInputFields();

        tableAccountingBook.setDisable(false);
        checkBoxIsNewEntryReoccurring.setSelected(false);
        handleReoccurrenceInputFields();
        rdButtonExpense.setSelected(true);
        categoryChoiceBoxForNewEntry.getSelectionModel().select(-1);

        btnEdit.setDisable(true);
        btnSaveEditedEntry.setDisable(true);
        btnCancelEdit.setDisable(true);
        btnAdd.setDisable(false);

        btnAddFutureBookings.setDisable(true);
        datePickerEndDateOfReoccurrence.setDisable(true);


        datePicker.setValue(DATE_OF_TODAY);
        tableAccountingBook.getSelectionModel().clearSelection();
    }

    /**
     * Fügt entsprechend der in der Klasse {@link ReoccurringBookingsHandler} beschriebenen Methode
     * {@link ReoccurringBookingsHandler#createEntriesBasedOnLocalDateList(BookEntry, ArrayList)} künftige Einträge für
     * Buchungen des Typs "wiederkehrend" hinzu.
     */
    @FXML
    private void addFutureReoccurringBookings() {

        ArrayList<LocalDate> datesList =
                ReoccurringBookingsHandler.getAllDatesOfFutureReoccurrences(
                        selectedEntryInTableView, datePickerEndDateOfReoccurrence.getValue());

        ArrayList<BookEntry> entries = ReoccurringBookingsHandler.createEntriesBasedOnLocalDateList(
                selectedEntryInTableView, datesList);

        for (BookEntry entry : entries) {
            BookEntryHolder.getInstance().getAccountingBook().add(entry);

        }
    }

//    private void setUpBarChart() {
//        NumberAxis yAxis = new NumberAxis();
//        CategoryAxis xAxis = new CategoryAxis();
//        barChartMonthlyBudget = new BarChart<>(xAxis, yAxis);
//
//
//        ArrayList<double[]> monthlyBalanceList =
//                getListOfMonthlyBalances(accountingBook, datePicker.getValue().getYear());
//
//        barChartMonthlyBudget.setTitle(String.valueOf(datePicker.getValue().getYear()));
//        xAxis.setLabel("Euro");
//        yAxis.setLabel("Monat");
//        xAxis.setTickLabelRotation(90);
//
//        XYChart.Series incomeSeries = new XYChart.Series();
//        incomeSeries.setName("Einnahmen");
//        XYChart.Series expensesSeries = new XYChart.Series();
//        expensesSeries.setName("Ausgaben");
//
//        incomeSeries.getData().add(new XYChart.Data<>(monthlyBalanceList.get(9)[0], MONTHS[9]));
//        incomeSeries.getData().add(new XYChart.Data<>(monthlyBalanceList.get(10)[0], MONTHS[10]));
//        incomeSeries.getData().add(new XYChart.Data<>("Jan.", 5000));
//
//
//        barChartMonthlyBudget.getData().addAll(incomeSeries);
//
//    }

    //endregion


}


