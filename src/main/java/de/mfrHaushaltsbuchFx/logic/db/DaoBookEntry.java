package de.mfrHaushaltsbuchFx.logic.db;

import de.mfrHaushaltsbuchFx.model.BookEntry;
import de.mfrHaushaltsbuchFx.model.EntryType;
import de.mfrHaushaltsbuchFx.settings.AppTexts;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasse zum Erstellen/Lesen/Updaten/Löschen von SQL Datenbank Einträgen aus {@link BookEntry}-Objekten bzw.
 * zum Auslesen von DB-Einträgen und dem Erzeugen von {@link BookEntry}-Objekten daraus.
 */
public class DaoBookEntry {
    //region Konstanten
    public static final String TEMPLATE_TABLE_NAME_ACCOUNTING_BOOK = "accountingbook2023";

    public static final String SQL_SELECT_ALL_ENTRIES =
            "SELECT * FROM " + TEMPLATE_TABLE_NAME_ACCOUNTING_BOOK;

    public static final String SQL_INSERT_NEW_ENTRY = "INSERT INTO " + TEMPLATE_TABLE_NAME_ACCOUNTING_BOOK +
                    " (type, interval_of_reoccurrence, amount, date, category, description) VALUES (?, ?, ?, ?, ?, ?)";

    public static final String SQL_SELECT_ENTRY_BY_ID =
            "SELECT * FROM " + TEMPLATE_TABLE_NAME_ACCOUNTING_BOOK + " WHERE id = ?";
    public static final String SQL_SELECT_ENTRY_BY_CATEGORY =
            "SELECT * FROM " + TEMPLATE_TABLE_NAME_ACCOUNTING_BOOK + " WHERE category = ?";
    public static final String SQL_SELECT_ENTRY_BY_TYPE =
            "SELECT * FROM " + TEMPLATE_TABLE_NAME_ACCOUNTING_BOOK + " WHERE type = ?";
    public static final String SQL_UPDATE_ENTRY =
            "UPDATE " + TEMPLATE_TABLE_NAME_ACCOUNTING_BOOK + " SET type = ?, interval_of_reoccurrence = ?," +
                    " amount = ?, date = ?, category = ?, description = ? WHERE id = ?";
    public static final String SQL_DELETE_ENTRY =
            "DELETE FROM " + TEMPLATE_TABLE_NAME_ACCOUNTING_BOOK + " WHERE id = ?";
    public static final String COL_TYPE = "type";
    public static final String COL_INTERVAL_OF_REOCC = "interval_of_reoccurrence";
    public static final String COL_AMOUNT = "amount";
    public static final String COL_DATE = "date";
    public static final String COL_CATEGORY = "category";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_ID = "id";
    public static final String COL_INSERT_ID = "insert_id";
    public static final int PARAMETER_INDEX_TYPE = 1;
    public static final int PARAMETER_INDEX_INTERVAL_OF_REOCC = 2;
    public static final int PARAMETER_INDEX_AMOUNT = 3;
    public static final int PARAMETER_INDEX_DATE = 4;
    public static final int PARAMETER_INDEX_CATEGORY = 5;
    public static final int PARAMETER_INDEX_DESCRIPTION = 6;
    public static final int PARAMETER_INDEX_ID = 7;
    public static final int PARAMETER_INDEX_OF_ID = 1;
    //endregion

    //region Attribute
    //endregion

    //region Methoden

    private static String insertTableNameIntoSqlStatement(String statement) {
        return statement; //TODO: Für spätere Schritte gedacht
    }

    /**
     * Aus einem {@link BookEntry} einen neuen DB-Eintrag in der DB-Tabelle erstellen.
     * @param dbConnection
     * @param bookEntry
     */
    public void create(Connection dbConnection, BookEntry bookEntry) {

        try (PreparedStatement statement = dbConnection.prepareStatement(
                SQL_INSERT_NEW_ENTRY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(PARAMETER_INDEX_TYPE, bookEntry.getEntryType().getTypeName());
            statement.setInt(PARAMETER_INDEX_INTERVAL_OF_REOCC, bookEntry.getEntryType().getInterval());
            statement.setDouble(PARAMETER_INDEX_AMOUNT, bookEntry.getAmount());

            //Umwandlung von LocalDate zu Date
            statement.setDate(PARAMETER_INDEX_DATE, Date.valueOf(bookEntry.getDate()));

            statement.setString(PARAMETER_INDEX_CATEGORY, bookEntry.getCategory());
            statement.setString(PARAMETER_INDEX_DESCRIPTION, bookEntry.getDescription());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();

            //neuen Schlüssel aus DB holen
            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(COL_INSERT_ID);
                bookEntry.setId(newId);
            }

        } catch (Exception e) {
            System.out.println(AppTexts.ERROR_CONNECTING_TO_DATABASE);
            e.printStackTrace();
        }
    }

    /**
     * Alle Elemente aus der DB-Tabelle auslesen und in eine ArrayList mit BookEntry-Objekten überführen.
     * @param dbConnection
     * @return
     */
    public List<BookEntry> getAllEntries(Connection dbConnection) {

        List<BookEntry> accountingBook = new ArrayList<>();

        try (PreparedStatement statement = dbConnection.prepareStatement(
                SQL_SELECT_ALL_ENTRIES)) {

            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {

                BookEntry currentBookEntry = createBookEntryFromResultSet(resultSet);

                accountingBook.add(currentBookEntry);
            }

        } catch (Exception e) {
            System.out.println(AppTexts.ERROR_READING_ALL_ENTRIES);
            e.printStackTrace();
        }

        return accountingBook;
    }

    private static BookEntry createBookEntryFromResultSet(ResultSet resultSet) throws SQLException {

        //Zuerst EntryType-Objekt aus DB-Daten erstellen
        String typeName = resultSet.getString(COL_TYPE);

        EntryType entryType = new EntryType(
                EntryType.getIndexOfBookingTypeByName(typeName),
                resultSet.getInt(COL_INTERVAL_OF_REOCC));

        //BookEntry zusammensetzen
        BookEntry currentBookEntry = new BookEntry(
                entryType,
                resultSet.getDouble(COL_AMOUNT),
                resultSet.getDate(COL_DATE).toLocalDate(),
                resultSet.getString(COL_CATEGORY),
                resultSet.getString(COL_DESCRIPTION)
        );

        currentBookEntry.setId(resultSet.getInt(COL_ID));
        return currentBookEntry;
    }

    /**
     * DB-Tabellen-Eintrag anhand des Primärschlüssels ID lesen und {@link BookEntry} daraus erzeugen.
     * @param dbConnection
     * @param id
     * @return Buchungseintrag BookEntry
     */
    public BookEntry getEntryById(Connection dbConnection, int id) {

        BookEntry bookEntry = null;

        try (PreparedStatement statement =
                dbConnection.prepareStatement(SQL_SELECT_ENTRY_BY_ID)) {
            statement.setInt(PARAMETER_INDEX_OF_ID, id);

            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            if (resultSet.first()) {

                bookEntry = createBookEntryFromResultSet(resultSet);
            }

        } catch (Exception e) {
            System.out.println(AppTexts.ERROR_READING_BY_ID);
            e.printStackTrace();
        }
        return bookEntry;
    }

    /**
     * Vorhandenen DB-Eintrag aktualisieren.
     * @param dbConnection
     * @param bookEntry
     */
    public void updateBookEntry(Connection dbConnection, BookEntry bookEntry) {

        try (PreparedStatement statement =
                dbConnection.prepareStatement(SQL_UPDATE_ENTRY)) {

            statement.setString(PARAMETER_INDEX_TYPE, bookEntry.getEntryType().getTypeName());
            statement.setInt(PARAMETER_INDEX_INTERVAL_OF_REOCC, bookEntry.getEntryType().getInterval());
            statement.setDouble(PARAMETER_INDEX_AMOUNT, bookEntry.getAmount());

            //Umwandlung von LocalDate zu Date
            statement.setDate(PARAMETER_INDEX_DATE, Date.valueOf(bookEntry.getDate()));

            statement.setString(PARAMETER_INDEX_CATEGORY, bookEntry.getCategory());
            statement.setString(PARAMETER_INDEX_DESCRIPTION, bookEntry.getDescription());

            //Index aus Objekt holen für "WHERE id = "
            statement.setInt(PARAMETER_INDEX_ID, bookEntry.getId());

            statement.executeUpdate();

        } catch (Exception e) {
            System.out.println(AppTexts.ERROR_UPDATING_ENTRY);
            e.printStackTrace();
        }
    }

    /**
     * DB-Eintrag aus der Tabelle löschen.
     * @param dbConnection
     * @param bookEntry
     */
    public void deleteBookEntry(Connection dbConnection, BookEntry bookEntry) {

        try (PreparedStatement statement =
                dbConnection.prepareStatement(SQL_DELETE_ENTRY)) {

            statement.setInt(PARAMETER_INDEX_OF_ID, bookEntry.getId());

            statement.executeUpdate();

        } catch (Exception e) {
            System.out.println(AppTexts.ERROR_DELETING_ENTRY);
            e.printStackTrace();
        }
    }
    //endregion

}



