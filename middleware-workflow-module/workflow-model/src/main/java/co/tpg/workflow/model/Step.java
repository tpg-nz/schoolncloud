package co.tpg.workflow.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.*;
import java.util.ArrayList;

/**
 * Model class to represents a workflow step.
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

    private ArrayList<StepField> steps;

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
    public Step(String id, String name, int sequence) {
        this.id = id;
        this.name = name;
        this.sequence = sequence;
        this.steps = new ArrayList<StepField>();
    }

    /**
     * Constructor with step fields
     * @param id            Workflow step UUID
     * @param name          Workflow step name
     * @param sequence      Workflow step sequence
     * @param stepFields    Workflow step fields
     */
    public Step(String id, String name, int sequence, ArrayList<StepField> stepFields) {
        this.id = id;
        this.name = name;
        this.sequence = sequence;

        // Clone step fields
        this.steps = new ArrayList<StepField>(stepFields.size());
        try {
            for (StepField stepField : stepFields) this.steps.add((StepField) stepField.clone());
        } catch (CloneNotSupportedException exc) {
            exc.printStackTrace();
        }
    }
}
