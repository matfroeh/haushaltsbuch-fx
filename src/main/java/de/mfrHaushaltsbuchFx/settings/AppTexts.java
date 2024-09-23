package de.mfrHaushaltsbuchFx.settings;

/**
 * In der GUI verwendete Texte, Textausgaben und (Fehler)-Meldungen
 */
public class AppTexts {
    //region Konstanten
    public static final String ERROR_INSERTING_INTO_DATABASE = "Error inserting into database.";
    public static final String ERROR_CONNECTING_TO_DATABASE = "Error connecting to database";
    public static final String ERROR_DELETING_ENTRY = "Error deleting entry.";
    public static final String ERROR_UPDATING_ENTRY = "Error updating entry.";
    public static final String ERROR_READING_BY_ID = "DaoBookEntry: Error reading entry by ID.";
    public static final String ERROR_READING_ALL_ENTRIES = "DaoBookEntry: Error getting db entries.";
    public static final String ERROR_REQUIRED_FIELDS_NOT_FILLED_BEFORE_SAVING = "\"Betrag\", \"Datum\", \"Kategorie\" (und \"Interval\" bei rglm. Buchungen) müssen zum Speichern ausgefüllt sein.";
    public static final String TXT_ENTRY_SAVED = "Eintrag erfolgreich gespeichert.";
    public static final String TXT_DELETION_COMPLETED = "Eintrag wurde erfolgreich gelöscht";
    public static final String TXT_ENTRY_CHANGED_AND_SAVED = "Eintrag wurde erfolgreich geändert und gespeichert.";
    public static final String TXT_EDIT_MODE_ACTIVE = "BEARBEITUNGSMODUS AKTIV";
    public static final String QUERY_DELETE_ENTRY = "Soll der folgende Eintrag gelöscht werden?";
    public static final String SCENE_TITLE_OVERVIEW = "MfrHaushaltsbuchFx - Gesamtübersicht";
    public static final String ERROR_LOADING_BUDGET_OVERVIEW_VIEW = "Error loading budgetOverview-view";
    public static final String ERROR_LOADING_CUSTOM_CATEGORY_CREATOR_VIEW = "Error loading customCategoryCreator-view";
    public static final String NAME_OF_CHOICE_BOX_FIELD_CREATE_NEW_CATEGORY = "<Neue Kategorie anlegen>";
    public static final String TXT_FILTER_ON = "Filterung aktiviert";
    public static final String FILTER_OFF = "Filterung ausgeschaltet";
    public static final String TEMPLATE_PRINT_SUMMARY_EXPENSES_INCOME = """
                                                  
            Gesamteinnahmen:            %.2f €
            - Gesamtausgaben:           %.2f €
            _____________________________________
                                        = %.2f €
            """;
    public static final String TEMPLATE_PRINT_SUMMARY_BY_CATEGORIES = """
            Summe der aktuell angezeigten Einträge:
                           
            %s = %.2f
             """;
    public static final String SCENE_TITLE_CATEGORY_CREATOR = "Neue Kategorie anlegen";
    public static final String TXT_CATEGORY_ALREADY_EXISTS = "Die Kategorie %s existiert bereits.";
    public static final String TXT_AMOUNT = "amount";
    public static final String TXT_DATE = "date";
    public static final String TXT_CATEGORY = "category";
    public static final String TXT_DESCRIPTION = "description";
    public static final String TXT_ENTRY_TYPE = "entryType";
    public static final String FX_BACKGROUND_COLOR_SALMON = "-fx-background-color: SALMON";
    public static final String FX_BACKGROUND_COLOR_PALEGREEN = "-fx-background-color: PALEGREEN";
    public static final String TEMPLATE_TABLE_FORMAT_FOR_DOUBLES = "%.2f";
    public static final String PAINT_GREEN = "green";
    public static final String PAINT_RED = "red";
    public static final String TEMPLATE_DATE_PATTERN_FOR_TABLE = "dd.MM.yyyy";
    public static final String PAINT_BLUE = "blue";
    public static final String TEMPLATE_MONTHLY_BALANCE = "%s: %.2f € - %.2f € = %.2f €\n";
    public static final String TXT_OUTPUT_MONTHLY_BALANCE = "Monatliche Bilanz für das aktuelle Jahr %d\n\n";
    public static final String TEMPLATE_BALANCE_OF_YEAR = "\nJahresbilanz: %.2f";
    public static final String[] MONTHS = new String[]{"Jan.", "Feb.", "Mär.", "Apr.", "Mai", "Jun.", "Jul.", "Aug.",
            "Sep.", "Okt.", "Nov.", "Dez."};
    //endregion

    //region Attribute
    //endregion

    //region Konstruktoren
    //endregion

    //region Methoden
    //endregion
}
