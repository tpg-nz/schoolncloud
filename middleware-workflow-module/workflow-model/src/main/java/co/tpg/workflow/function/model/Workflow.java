package co.tpg.workflow.function.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@AllArgsConstructor
@NoArgsConstructor
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

    @JsonIgnore
    @DynamoDBIgnore
    //@JsonManagedReference
    private List<Step> steps;
}
