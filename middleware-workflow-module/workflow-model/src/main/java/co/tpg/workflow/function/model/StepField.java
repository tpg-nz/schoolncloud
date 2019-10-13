package co.tpg.workflow.function.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

/**
 * Model class to represents a workflow step field
 * @author Andrej
 * @since 2019-10-08
 */
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@DynamoDBTable(tableName = "StepField")
public class StepField extends AbstractModel<String> {

    @EqualsAndHashCode.Include
    @DynamoDBHashKey(attributeName = "id")
    private String id;
    @DynamoDBAttribute(attributeName = "label")
    private String label;
    @DynamoDBAttribute(attributeName = "sequence")
    private int sequence;

    @DynamoDBIgnore
    @JsonIgnore
    private Step step;

    @DynamoDBTypeConverted(converter = FieldTypeConverter.class)
    @DynamoDBAttribute(attributeName = "fieldType")
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
     * @param step      Workflow step object
     * @param fieldType Workflow step field type
     */
    public StepField(String id, String label, int sequence, Step step, FieldType fieldType) {
        this.id = id;
        this.label = label;
        this.sequence = sequence;
        this.step = step;
        this.fieldType = fieldType;
    }

    /**
     * The stepId used in DB for proper reference
     * @return  Step Id
     */
    @DynamoDBAttribute(attributeName = "stepId")
    @JsonIgnore
    public String getStepId() {
        return (this.step != null) ? this.step.getId() : null;
    }

    /**
     * Sets the stepID
     * @param stepId    Step Id
     */
    public void setStepId(String stepId) {
        if (this.step == null) {
            this.step = new Step();
        }
        this.step.setId(stepId);
    }
}
