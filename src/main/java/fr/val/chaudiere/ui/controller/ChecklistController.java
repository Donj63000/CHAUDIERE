package fr.val.chaudiere.ui.controller;

import fr.val.chaudiere.model.Field;
import fr.val.chaudiere.model.FieldType;
import fr.val.chaudiere.model.Checklist;
import fr.val.chaudiere.storage.ChecklistStorage;
import fr.val.chaudiere.ui.factory.FieldFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrôleur principal : charge les champs dynamiquement,
 * enregistre les valeurs et les persiste en JSON.
 */
public class ChecklistController {

    @FXML private VBox root;

    private final List<Field> defs = List.of(
        new Field("Niveau d'eau OK ?", FieldType.BOOLEAN),
        new Field("Niveau d'eau voyant OK ?", FieldType.BOOLEAN),
        new Field("Pression OK ?", FieldType.BOOLEAN),
        new Field("Température bâche ?", FieldType.DECIMAL, "°C"),
        new Field("Purge OK ?", FieldType.BOOLEAN)
    );

    @FXML
    public void initialize() {
        defs.forEach(f -> root.getChildren().add(FieldFactory.createControl(f)));
    }

    @FXML
    private void onSave() {
        ChecklistStorage.save(new Checklist(LocalDateTime.now(), defs));
        new Alert(AlertType.INFORMATION, "Enregistré !").showAndWait();
    }
}
