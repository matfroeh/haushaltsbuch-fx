module de.mfrHaushaltsbuchFx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.mariadb.jdbc;



    opens de.mfrHaushaltsbuchFx to javafx.fxml;
    exports de.mfrHaushaltsbuchFx;
    exports de.mfrHaushaltsbuchFx.gui;
    opens de.mfrHaushaltsbuchFx.gui to javafx.fxml;
    exports de.mfrHaushaltsbuchFx.test;
    opens de.mfrHaushaltsbuchFx.test;
    exports de.mfrHaushaltsbuchFx.model;
    opens de.mfrHaushaltsbuchFx.model to javafx.fxml;
    exports de.mfrHaushaltsbuchFx.logic;
    opens de.mfrHaushaltsbuchFx.logic to javafx.fxml;
}