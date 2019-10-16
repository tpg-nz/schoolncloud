package co.tpg.workflow.function.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
/**
 * Field type converter class
 * @author Andrej
 * @since 2019-10-08
 */
public class FieldTypeConverter implements DynamoDBTypeConverter<String, FieldType> {
    /**
     * Field type from enum to string converter
     * @param fieldType Field type object
     * @return  Converted field type in string format
     */
    @Override
    public String convert(FieldType fieldType) {
        return (fieldType != null ) ? fieldType.toString() : null;
    }

    /**
     * Field type from string to enum converter
     * @param fieldTypeLabel    Field type string
     * @return  Converted field type in Enum format
     */
    @Override
    public FieldType unconvert(String fieldTypeLabel) {
        return (fieldTypeLabel != null) ? FieldType.getFieldType(fieldTypeLabel) : null;
    }
}
