package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Générateur de squelette pour l’application « Contrôle chaudière ».
 * Compile :   javac Main.java
 * Exécute :   java  Main
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Map<String, String> files = Map.of(
                // --- UI --------------------------------------------
                "src/main/java/fr/val/chaudiere/ui/MainApp.java", MAIN_APP(),
                "src/main/java/fr/val/chaudiere/ui/controller/ChecklistController.java", CONTROLLER(),
                "src/main/java/fr/val/chaudiere/ui/factory/FieldFactory.java", FIELD_FACTORY(),
                // --- Domaine / Modèle ------------------------------
                "src/main/java/fr/val/chaudiere/model/Field.java", FIELD(),
                "src/main/java/fr/val/chaudiere/model/FieldType.java", FIELD_TYPE(),
                "src/main/java/fr/val/chaudiere/model/Checklist.java", CHECKLIST(),
                // --- Persistance & utilitaires ----------------------
                "src/main/java/fr/val/chaudiere/storage/ChecklistStorage.java", STORAGE(),
                "src/main/java/fr/val/chaudiere/util/ValidationUtil.java", VALIDATION_UTIL()
        );

        for (var entry : files.entrySet()) {
            write(entry.getKey(), entry.getValue());
        }
        System.out.println("✅ Génération terminée le " + LocalDateTime.now());
    }

    // ---------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------
    private static void write(String pathString, String content) throws IOException {
        Path p = Path.of(pathString);
        Files.createDirectories(p.getParent());
        Files.writeString(
                p,
                content,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        );
        System.out.println("  → " + p);
    }

    // ---------------------------------------------------------
    // Contenus des fichiers générés (Java 15+ : Text Blocks)
    // ---------------------------------------------------------
    private static String MAIN_APP() { return """
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("checklist.fxml"));
                Scene scene = new Scene(loader.load());
                stage.setTitle("Contrôle chaudière");
                stage.setScene(scene);
                stage.show();
            }
        
            public static void main(String[] args) {
                launch();
            }
        }
        """; }

    private static String CONTROLLER() { return """
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
        """; }

    private static String FIELD_FACTORY() { return """
        package fr.val.chaudiere.ui.factory;
        
        import fr.val.chaudiere.model.Field;
        import fr.val.chaudiere.model.FieldType;
        import fr.val.chaudiere.util.ValidationUtil;
        import javafx.beans.value.ChangeListener;
        import javafx.scene.Node;
        import javafx.scene.control.CheckBox;
        import javafx.scene.control.TextField;
        import javafx.scene.control.Tooltip;
        import javafx.util.converter.NumberStringConverter;
        
        /**
         * Fabrique les contrôles JavaFX correspondant à un Field.
         */
        public final class FieldFactory {
        
            private FieldFactory() { /* static only */ }
        
            public static Node createControl(Field f) {
                return switch (f.getType()) {
                    case BOOLEAN -> createBoolean(f);
                    case DECIMAL -> createDecimal(f);
                    case TEXT   -> createText(f);
                };
            }
        
            private static Node createBoolean(Field f) {
                CheckBox cb = new CheckBox(f.getLabel());
                cb.selectedProperty().addListener((obs, o, n) -> f.setValue(n));
                return cb;
            }
        
            private static Node createDecimal(Field f) {
                TextField tf = new TextField();
                tf.setPromptText(f.getLabel() + (f.getUnit() == null ? "" : " (" + f.getUnit() + ")"));
                tf.setTextFormatter(new javafx.scene.control.TextFormatter<>(new NumberStringConverter()));
                ChangeListener<String> listener = (obs, o, n) -> {
                    if (ValidationUtil.isDecimal(n)) f.setValue(Double.parseDouble(n));
                };
                tf.textProperty().addListener(listener);
                tf.setTooltip(new Tooltip("Nombre décimal requis"));
                return tf;
            }
        
            private static Node createText(Field f) {
                TextField tf = new TextField();
                tf.setPromptText(f.getLabel());
                tf.textProperty().addListener((obs, o, n) -> f.setValue(n));
                return tf;
            }
        }
        """; }

    private static String FIELD() { return """
        package fr.val.chaudiere.model;
        
        /**
         * Définition d’un champ saisi par l’opérateur.
         */
        public class Field {
            private final String label;
            private final FieldType type;
            private final String unit;
            private Object value;
        
            public Field(String label, FieldType type) { this(label, type, null); }
        
            public Field(String label, FieldType type, String unit) {
                this.label = label;
                this.type  = type;
                this.unit  = unit;
            }
        
            // getters
            public String getLabel() { return label; }
            public FieldType getType() { return type; }
            public String getUnit() { return unit; }
            public Object getValue() { return value; }
        
            // setter
            public void setValue(Object value) { this.value = value; }
        }
        """; }

    private static String FIELD_TYPE() { return """
        package fr.val.chaudiere.model;
        
        /**
         * Typage minimal des champs.
         */
        public enum FieldType { BOOLEAN, DECIMAL, TEXT }
        """; }

    private static String CHECKLIST() { return """
        package fr.val.chaudiere.model;
        
        import java.time.LocalDateTime;
        import java.util.List;
        
        /**
         * Une inspection complète de chaudière.
         */
        public record Checklist(LocalDateTime date, List<Field> fields) { }
        """; }

    private static String STORAGE() { return """
        package fr.val.chaudiere.storage;
        
        import com.fasterxml.jackson.databind.ObjectMapper;
        import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
        import fr.val.chaudiere.model.Checklist;
        
        import java.io.IOException;
        import java.io.UncheckedIOException;
        import java.nio.file.Files;
        import java.nio.file.Path;
        import java.nio.file.Paths;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;
        
        /**
         * Sauvegarde les check-lists en JSON dans un fichier local.
         */
        public final class ChecklistStorage {
        
            private static final Path FILE = Paths.get("inspections.json");
            private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());
        
            private ChecklistStorage() { }
        
            public static void save(Checklist cl) {
                List<Checklist> all = loadAll();
                all.add(cl);
                try {
                    MAPPER.writerWithDefaultPrettyPrinter().writeValue(FILE.toFile(), all);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        
            public static List<Checklist> loadAll() {
                if (!Files.exists(FILE)) return new ArrayList<>();
                try {
                    return Arrays.asList(MAPPER.readValue(FILE.toFile(), Checklist[].class));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }
        """; }

    private static String VALIDATION_UTIL() { return """
        package fr.val.chaudiere.util;
        
        /**
         * Validation simple de chaînes.
         */
        public final class ValidationUtil {
            private ValidationUtil() { }
        
            public static boolean isDecimal(String s) {
                if (s == null || s.isBlank()) return false;
                try { Double.parseDouble(s); return true; }
                catch (NumberFormatException e) { return false; }
            }
        }
        """; }
}
