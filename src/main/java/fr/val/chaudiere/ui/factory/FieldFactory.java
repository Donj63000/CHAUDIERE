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
