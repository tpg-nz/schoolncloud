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
public class Step extends AbstractModel<String> {

    @EqualsAndHashCode.Include
    @DynamoDBHashKey(attributeName = "id")
    private String id;
    @DynamoDBAttribute(attributeName = "name")
    private String name;
    @DynamoDBAttribute(attributeName = "sequence")
    private int sequence;

    // TODO -> fix the object storing
    //@JsonBackReference
    @DynamoDBIgnore
    @JsonIgnore
    private Workflow workflow;

    @DynamoDBIgnore
    @JsonManagedReference
    private List<StepField> stepFields;


    /**
     * Default constructor
     */
    public Step() { }

    /**
     * Full constructor
     * @param id            Workflow step UUID
     * @param name          Workflow step name
     * @param sequence      Workflow step sequence
     * @param workflow      Workflow
     * @param stepFields    Workflow step fields
     */
    public Step(String id, String name, int sequence, Workflow workflow, List<StepField> stepFields) {
        this.id = id;
        this.name = name;
        this.sequence = sequence;
        this.workflow = workflow;
        this.stepFields = stepFields;
    }

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
     * Workflow id setter
     * @param workflowId    Workflow Id
     */
    public void setWorkflowId(String workflowId) {
        if (this.workflow == null) {
            this.workflow = new Workflow();
        }
        this.workflow.setId(workflowId);
    }
}