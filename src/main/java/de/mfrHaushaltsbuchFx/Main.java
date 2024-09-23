package de.mfrHaushaltsbuchFx;

import de.mfrHaushaltsbuchFx.gui.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * Anwendung: HaushaltsbuchFX.
 * <p>
 * Funktionalitäten:
 * </p>
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
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneManager.getInstance().setMainStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}