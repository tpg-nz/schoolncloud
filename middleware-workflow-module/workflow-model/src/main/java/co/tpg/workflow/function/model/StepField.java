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
@NoArgsConstructor
@AllArgsConstructor
public class StepField extends AbstractModel<String> {

    @EqualsAndHashCode.Include
    @DynamoDBHashKey(attributeName = "id")
    private String id;
    @DynamoDBAttribute(attributeName = "label")
    private String label;
    @DynamoDBAttribute(attributeName = "sequence")
    private int sequence;

    @JsonIgnore
    @DynamoDBIgnore
    private Step step;

    @JsonIgnore
    @DynamoDBTypeConverted(converter = FieldTypeConverter.class)
    @DynamoDBAttribute(attributeName = "fieldType")
    private FieldType fieldType;

    /**
     * The stepId used in DB for proper reference
     * @return  Step Id
     */
    @JsonIgnore
    @DynamoDBAttribute(attributeName = "stepId")
    public String getStepId() {
        return (this.step != null) ? this.step.getId() : null;
    }

    /**
     * Sets the step Id in the step object
     * @param stepId    Step Id used in the DB
     */
    public void setStepId(String stepId) {
        if (this.step == null) {
            this.step = new Step();
        }
        this.step.setId(stepId);
    }
}
