package co.tpg.catalog.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Response class for Address entity
 * 
 * @author maxx
 * @since 2019-10-11
 */

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "Address")
public class Address extends AbstractModel<String> {

    @DynamoDBHashKey(attributeName = "id")
    private String id;

    @EqualsAndHashCode.Exclude
    @DynamoDBAttribute(attributeName = "address")
    private String address;

    @EqualsAndHashCode.Exclude
    @DynamoDBAttribute(attributeName = "zipCode")
    private String zipCode;

    @EqualsAndHashCode.Exclude
    @DynamoDBAttribute(attributeName = "city")
    private String city;

    @EqualsAndHashCode.Exclude
    @DynamoDBAttribute(attributeName = "country")
    private String country;

    @EqualsAndHashCode.Exclude
    @DynamoDBAttribute(attributeName = "addressType")
    private String addressType;

    @EqualsAndHashCode.Exclude
    @DynamoDBAttribute(attributeName = "contactType")
    private String contactType;

}