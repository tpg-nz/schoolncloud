package co.tpg.workflow.function.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.List;

/**
 * Model class represents a workflow step
 * @author Andrej
 * @since 2019-10-08
 */
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@DynamoDBTable(tableName = "Step")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class Step extends AbstractModel<String> {

    @EqualsAndHashCode.Include
    @DynamoDBHashKey(attributeName = "id")
    private String id;
    @DynamoDBAttribute(attributeName = "name")
    private String name;
    @DynamoDBAttribute(attributeName = "sequence")
    private int sequence;

    @DynamoDBIgnore
    @JsonIgnore
    private Workflow workflow;

    @JsonIgnore
    @DynamoDBIgnore
    private List<StepField> stepFields;

    /**
     * Workflow id getter is used in DB for proper reference
     * @return  Workflow Id
     */
    @DynamoDBAttribute(attributeName = "workflowId")
    @JsonIgnore
    public String getWorkflowId() {
        return (this.workflow != null) ? this.workflow.getId() : null;
    }

    /**
     * Sets the workflow Id in the workflow object
     * @param workflowId    Workflow Id used in DB
     */
    public void setWorkflowId(String workflowId) {
        if (this.workflow == null) {
            this.workflow = new Workflow();
        }
        this.workflow.setId(workflowId);
    }

    public Step() {}
}