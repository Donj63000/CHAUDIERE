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
