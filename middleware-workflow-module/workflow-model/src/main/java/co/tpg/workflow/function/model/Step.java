package co.tpg.workflow.function.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class represents a workflow step.
 * @author Andrej
 * @since 2019-10-08
 */
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@DynamoDBTable(tableName = "Step")
public class Step extends AbstractModel<String> {

    @DynamoDBHashKey(attributeName = "id")
    private String id;
    @DynamoDBAttribute(attributeName = "name")
    private String name;
    @DynamoDBAttribute(attributeName = "sequence")
    private int sequence;

    private Workflow workflow;
    private ArrayList<StepField> stepFields = new ArrayList<StepField>();


    /**
     * Default constructor
     */
    public Step() { }

    /**
     * Main constructor
     * @param id        Workflow step UUID
     * @param name      Workflow step name
     * @param sequence  Workflow step sequence
     */
    public Step(String id, String name, int sequence, Workflow workflow) {
        this.id = id;
        this.name = name;
        this.sequence = sequence;
        this.workflow = workflow;
    }

    /**
     * Constructor with step fields
     * @param id            Workflow step UUID
     * @param name          Workflow step name
     * @param sequence      Workflow step sequence
     * @param workflow      Workflow
     * @param stepFields    Workflow step fields
     */
    public Step(String id, String name, int sequence, Workflow workflow, ArrayList<StepField> stepFields) {
        this.id = id;
        this.name = name;
        this.sequence = sequence;
        this.workflow = workflow;
        this.stepFields = cloneSteps(stepFields);
    }

    public void setSteps(List<StepField> steps) {
        this.stepFields = cloneSteps(steps);
    }

    public ArrayList<StepField> getSteps() {
        return this.stepFields;
    }

    private ArrayList<StepField> cloneSteps(List<StepField> stepFields)  {
        // Clone step fields
        ArrayList<StepField> steps = new ArrayList<StepField>(stepFields.size());
        steps.addAll(stepFields);
        return steps;
    }
}
