package co.tpg.workflow.function.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DynamoDBTable(tableName = "StepField")
@JsonIgnoreProperties({"step"})
public class StepField extends AbstractModel<String> implements Cloneable {
    @EqualsAndHashCode.Include
    @DynamoDBHashKey(attributeName = "id")
    private String id;
    @DynamoDBAttribute(attributeName = "label")
    private String label;
    @DynamoDBAttribute(attributeName = "sequence")
    private int sequence;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @DynamoDBIgnore
    private Step step;

    // TODO -> confirmation needed
    @EqualsAndHashCode.Exclude
    @DynamoDBIgnore
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


    @DynamoDBAttribute(attributeName = "stepId")
    public String getStepId() {
        return (this.step != null) ? this.step.getId() : null;
    }

    public void setStepId(String stepId) {
        if (this.step == null) {
            this.step = new Step();
        }
        this.step.setId(stepId);
    }
}
