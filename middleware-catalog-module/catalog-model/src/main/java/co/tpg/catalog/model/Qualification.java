package co.tpg.catalog.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

/**
 * Model class to represent a Qualification
 * @author James
 * @since 2019-10-12
 */

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@DynamoDBTable(tableName = "Qualification")
public class Qualification extends AbstractModel<String> {
    @DynamoDBHashKey(attributeName = "id")
    private String id;
    @EqualsAndHashCode.Exclude
    @DynamoDBAttribute(attributeName = "name")
    private String name;
    @EqualsAndHashCode.Exclude
    @DynamoDBAttribute(attributeName = "hyperlink")
    private String hyperlink;
    @EqualsAndHashCode.Exclude
    @DynamoDBIgnore
    private Subject subject;

    public Qualification() {
    }

    public Qualification(String id, String name, String hyperlink, Subject subject) {
        this.id = id;
        this.name = name;
        this.hyperlink = hyperlink;
        this.subject = subject;
    }

    @JsonIgnore
    @DynamoDBAttribute(attributeName = "subjectId")
    public String getSubjectId() {
        return this.subject.getId();
    }
    public void setSubjectId(String subjectId) {
        if( this.subject == null ) {
            this.subject = new Subject();
        }
        this.subject.setId(subjectId);
    }
}