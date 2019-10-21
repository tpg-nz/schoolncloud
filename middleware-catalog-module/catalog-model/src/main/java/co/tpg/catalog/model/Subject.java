package co.tpg.catalog.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Response class for Subject entity
 * 
 * @author maxx
 * @since 2019-10-03
 */

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "Subject")
public class Subject extends AbstractModel<String> {

    @DynamoDBHashKey(attributeName = "id")
    private String id;

    @EqualsAndHashCode.Exclude
    private String name;

    @EqualsAndHashCode.Exclude
    private String overview;

    @EqualsAndHashCode.Exclude
    private int level;
}
