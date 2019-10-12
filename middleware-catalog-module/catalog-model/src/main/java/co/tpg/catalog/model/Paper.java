package co.tpg.catalog.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.*;

/**
 * Model class to represent a Paper.
 * @author Rod
 * @since 2019-10-09
 */
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DynamoDBTable(tableName = "Paper")
public class Paper extends AbstractModel<String> {
    @EqualsAndHashCode.Include
    @DynamoDBHashKey(attributeName = "id")
    private String id;
    @DynamoDBAttribute(attributeName = "year")
    private int year;
    @DynamoDBAttribute(attributeName = "points")
    private int points;
    @DynamoDBAttribute(attributeName = "teachingPeriod")
    private String teachingPeriod;

    public Paper() {
    }

    public Paper(String id) {
        this.id = id;
    }
}
