package co.tpg.catalog.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Response class for Subject entity
 * 
 * @author maxx
 * @since 2019-10-03
 */

@Data
@DynamoDBTable(tableName = "Subject")
public class Subject extends AbstractModel<String> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @DynamoDBHashKey(attributeName = "id")
    private String id;

    @EqualsAndHashCode.Exclude
    @DynamoDBHashKey(attributeName = "name")
    private String name;

    @EqualsAndHashCode.Exclude
    @DynamoDBIgnore
    private Subject overview;

    @EqualsAndHashCode.Exclude
    @DynamoDBHashKey(attributeName = "level")
    private int level;

    public Subject() {
    }

    public Subject(String id, String name, Subject overview, int level) {
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.level = level;
    }

    @JsonIgnore
    @DynamoDBAttribute(attributeName = "overviewId")
    public String getOverviewId() {
        return this.overview.getId();
    }

    public void setOverviewId(String overviewId) {
        this.overview.setId(overviewId);
    }

}