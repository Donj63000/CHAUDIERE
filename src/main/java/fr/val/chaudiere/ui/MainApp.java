package fr.val.chaudiere.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Point d’entrée JavaFX.
 */
public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Chargement via un chemin absolu pour garantir la présence du FXML
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fr/val/chaudiere/ui/checklist.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Contrôle chaudière");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
