package co.tpg.workflow.function.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
/**
 * Field type converter class
 * @author Andrej
 * @since 2019-10-08
 */
public class FieldTypeConverter implements DynamoDBTypeConverter<String, FieldType> {

    @Override
    public String convert(FieldType fieldType) {
        return (fieldType != null ) ? fieldType.toString() : null;
    }

    @Override
    public FieldType unconvert(String fieldTypeLabel) {
        return (fieldTypeLabel != null) ? FieldType.getFieldType(fieldTypeLabel) : null;
    }
}
