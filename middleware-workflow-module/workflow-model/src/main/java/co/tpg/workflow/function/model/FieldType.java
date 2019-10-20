package co.tpg.workflow.function.model;

/**
 * Enum class represents the workflow field type
 * @author Andrej
 * @since 2019-10-08
 */

public enum FieldType {

    TEXTFIELD("Textfield"),
    TEXT_AREA("TextArea"),
    COMBOBOX("Combobox"),
    CHECKBOX("Checkbox"),
    RADIOBOX("Radiobox");

    public final String label;

    FieldType(String label) {
        this.label = label;
    }

    public String toString() {
        return this.label;
    }

    public static FieldType getFieldType(String label) {
        for (FieldType fieldType : values()) {
            if (fieldType.label.equals(label)) {
                return fieldType;
            }
        }
        return null;
    }
}
