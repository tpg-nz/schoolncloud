package co.tpg.catalog.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.*;

/**
 * Model class to represent Campuses.
 * @author Rod
 * @since 2019-09-17
 */
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DynamoDBTable(tableName = "Campus")
public class Campus extends AbstractModel<String> {
    @EqualsAndHashCode.Include
    @DynamoDBHashKey(attributeName = "id")
    private String id;
    @DynamoDBAttribute(attributeName = "name")
    private String name;

    public Campus() {
    }

    public Campus(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
