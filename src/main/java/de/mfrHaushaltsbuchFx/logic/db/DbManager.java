package de.mfrHaushaltsbuchFx.logic.db;

import de.mfrHaushaltsbuchFx.model.BookEntry;
import de.mfrHaushaltsbuchFx.settings.AppTexts;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

/**
 * Schnittstelle zur Datenbank "accountingbook". Singleton.
 */

public class DbManager {
    //region Konstanten
    public static final String SERVER_IP = "localhost";
    public static final String DB_NAME = "accountingbook";
    public static final String CONNECTION_URL = "jdbc:mariadb://" + SERVER_IP + "/"
            + DB_NAME;
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "";
    //endregion

    //region Attribute
    private static DbManager instance;
    private DaoBookEntry daoBookEntry;
    //endregion

    //region Konstruktoren
    private DbManager() {
        daoBookEntry = new DaoBookEntry();
    }
    //endregion

    //region Methoden
    public static synchronized DbManager getInstance() {
        if (instance == null) instance = new DbManager();
        return instance;
    }

    private Connection getDbConnection() {
        Connection dbConnection = null;

        try {
            dbConnection = DriverManager.getConnection(CONNECTION_URL, DB_USERNAME, DB_PASSWORD);

        } catch (Exception e) {
            System.out.println(AppTexts.ERROR_CONNECTING_TO_DATABASE);
            e.printStackTrace();
        }

        return dbConnection;
    }

    public void insertData(Object object) {
        if (object instanceof BookEntry bookEntry) {
            daoBookEntry.create(getDbConnection(), bookEntry);
        } else {
            System.out.println(AppTexts.ERROR_INSERTING_INTO_DATABASE);
        }
    }

    public BookEntry getBookEntryById(int id) {
        return daoBookEntry.getEntryById(getDbConnection(), id);
    }

    public List<BookEntry> getAllBookEntries() {
        return daoBookEntry.getAllEntries(getDbConnection());
    }

    public void updateBookEntry(BookEntry bookEntry) {
        daoBookEntry.updateBookEntry(getDbConnection(), bookEntry);
    }

    public void deleteBookEntry(BookEntry bookEntry) {
        daoBookEntry.deleteBookEntry(getDbConnection(), bookEntry);
    }
    //endregion
}
