package co.tpg.workflow.function.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

/**
 * Model class to represents a workflow step field.
 * @author Andrej
 * @since 2019-10-08
 */

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@DynamoDBTable(tableName = "StepField")
public class StepField extends AbstractModel<String> implements Cloneable {

    @DynamoDBHashKey(attributeName = "id")
    private String id;
    @DynamoDBAttribute(attributeName = "label")
    private String label;
    @DynamoDBAttribute(attributeName = "sequence")
    private int sequence;

//    @DynamoDBIndexRangeKey( attributeName="parentId",
//                            localSecondaryIndexName="parentIdIndex")
//    private String parentId;
    private Step step;
    private FieldType fieldType;

    /**
     * Default constructor
     */
    public StepField() { }

    /**
     * Main constructor
     * @param id        Workflow step field UUID
     * @param label     Workflow step field label
     * @param sequence  Workflow step field sequence
     */
    public StepField(String id, String label, int sequence, Step step, FieldType fieldType) {
        this.id = id;
        this.label = label;
        this.sequence = sequence;
        this.step = step;
        this.fieldType = fieldType;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
