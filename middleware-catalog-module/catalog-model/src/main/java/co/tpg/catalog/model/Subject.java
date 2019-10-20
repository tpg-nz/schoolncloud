package co.tpg.catalog.model;

<<<<<<< HEAD
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
=======
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Model class to represent a Subject
 * @author James
 * @since 2019-09-16
 */

@Builder
@Getter
@Setter
@ToString
public class Subject {
    private String id;
    private String name;
    private String overview;
>>>>>>> 4265c7895b27ca771bb35573ea5cf2b8405870cb
    private int level;

    public Subject() {
    }

<<<<<<< HEAD
    public Subject(String id, String name, Subject overview, int level) {
=======
    public Subject(String id, String name, String overview, int level) {
>>>>>>> 4265c7895b27ca771bb35573ea5cf2b8405870cb
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.level = level;
    }
<<<<<<< HEAD

    @JsonIgnore
    @DynamoDBAttribute(attributeName = "overviewId")
    public String getOverviewId() {
        return this.overview.getId();
    }

    public void setOverviewId(String overviewId) {
        this.overview.setId(overviewId);
    }

}
=======
}
>>>>>>> 4265c7895b27ca771bb35573ea5cf2b8405870cb
