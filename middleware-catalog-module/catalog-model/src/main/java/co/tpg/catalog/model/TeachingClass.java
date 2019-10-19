package co.tpg.catalog.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

/**
 * Model class to represent a Teaching Class (Classroom).
 * @author Rod
 * @since 2019-09-28
 */
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@DynamoDBTable(tableName = "TeachingClass")
public class TeachingClass extends AbstractModel<String> {
    @DynamoDBHashKey(attributeName = "id")
    private String id;
    @EqualsAndHashCode.Exclude
    @DynamoDBAttribute(attributeName = "code")
    private String code;
    @EqualsAndHashCode.Exclude
    @DynamoDBAttribute(attributeName = "year")
    private int year;
    @EqualsAndHashCode.Exclude
    @DynamoDBAttribute(attributeName = "semester")
    private int semester;
    @EqualsAndHashCode.Exclude
    @DynamoDBIgnore
    private Paper paper;
    @EqualsAndHashCode.Exclude
    @DynamoDBIgnore
    private Campus campus;

    public TeachingClass() {
    }

    public TeachingClass(String id, String code, int year, int semester, Paper paper, Campus campus) {
        this.id = id;
        this.code = code;
        this.year = year;
        this.semester = semester;
        this.paper = paper;
        this.campus = campus;
    }

    @JsonIgnore
    @DynamoDBAttribute(attributeName = "paperId")
    public String getPaperId() {
        return this.paper.getId();
    }
    public void setPaperId(String paperId) {
        if( this.paper == null ) {
            this.paper = new Paper();
        }
        this.paper.setId(paperId);
    }

    @JsonIgnore
    @DynamoDBAttribute(attributeName = "campusId")
    public String getCampusId() {
        return this.campus.getId();
    }
    public void setCampusId(String campusId) {
        if( this.campus == null ) {
            this.campus = new Campus();
        }
        this.campus.setId(campusId);
    }
}
