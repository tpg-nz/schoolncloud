package co.tpg.workflow.function.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class represents a workflow.
 * @author Andrej
 * @since 2019-10-12
 */
@Builder
@Getter
@Setter
@ToString
public class Workflow extends AbstractModel<String> {

    @DynamoDBHashKey(attributeName = "id")
    private String id;
    @DynamoDBAttribute(attributeName = "name")
    private String name;
    @DynamoDBAttribute(attributeName = "description")
    private String descritpion;
    @DynamoDBAttribute(attributeName = "enabled")
    private Boolean enabled;
    @DynamoDBAttribute(attributeName = "version")
    private String version;
    @DynamoDBIgnore
    private ArrayList<Step> steps;

    /**
     * Default constructor
     */
    public Workflow() {}

    /**
     * @param id
     * @param name
     * @param descritpion
     * @param enabled
     * @param version
     * @param steps
     */
    public Workflow(String id, String name, String descritpion, Boolean enabled, String version, ArrayList<Step> steps) {
        this.id = id;
        this.name = name;
        this.descritpion = descritpion;
        this.enabled = enabled;
        this.version = version;
        this.steps = cloneSteps(steps);
    }

    /**
     * Clone the steps into a new workflow list
     * @param steps Workflow steps
     * @return      Workflow steps in an array list
     */
    private ArrayList<Step> cloneSteps(final List<Step> steps)  {
        ArrayList<Step> workflowSteps = new ArrayList<Step>(steps.size());
        workflowSteps.addAll(steps);
        return workflowSteps;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(final List<Step> steps) {
        this.steps = cloneSteps(steps);
    }
}
