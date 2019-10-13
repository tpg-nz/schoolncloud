package co.tpg.workflow.function.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.util.List;

/**
 * Model class represents a workflow
 * @author Andrej
 * @since 2019-10-12
 */
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@DynamoDBTable(tableName = "Workflow")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Workflow extends AbstractModel<String> {

    @EqualsAndHashCode.Include
    @DynamoDBHashKey(attributeName = "id")
    private String id;
    @DynamoDBAttribute(attributeName = "name")
    private String name;
    @DynamoDBAttribute(attributeName = "description")
    private String description;
    @DynamoDBAttribute(attributeName = "enabled")
    private Boolean enabled;
    @DynamoDBAttribute(attributeName = "version")
    private String version;

    @DynamoDBIgnore
    @JsonManagedReference
    private List<Step> steps;

    /**
     * Default constructor
     */
    public Workflow() {}

    /**
     * Main workflow constructor
     * @param id            Workflow Id
     * @param name          Workflow name
     * @param description   Workflow description
     * @param enabled       Workflow enabled flag
     * @param version       Workflow version
     * @param steps         Workflow steps container
     */
    public Workflow(String id, String name, String description, Boolean enabled, String version, List<Step> steps) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.enabled = enabled;
        this.version = version;
        this.steps = steps;
    }
}
