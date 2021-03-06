package co.tpg.catalog.dao;

import co.tpg.catalog.dao.exception.BackendException;
import co.tpg.catalog.model.Campus;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * DAO class to persist Paper into the backend.
 * @author Rod
 * @since 2019-10-09
 */
public class CampusDAO implements DAO<Campus,String>{
    private static final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
    private static final String DYNAMO_TABLE_NAME = "Campus";
    private static final DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

    @Override
    public Campus create(Campus campus) throws BackendException {
        try {
            campus.setId(UUID.randomUUID().toString());
            mapper.save(campus);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
        return campus;
    }

    @Override
    public Campus retrieveById(String key) throws BackendException {
        final Campus paper;

        try {
            paper = mapper.load(Campus.class,key);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }

        return paper;
    }

    @Override
    public List<Campus> retrieveAll(String lastEvaluatedKey, int pageSize) throws BackendException {
        final Map<String, AttributeValue> map = new HashMap<>();
        final DynamoDBScanExpression paginatedExpression = new DynamoDBScanExpression()
                .withLimit(pageSize);
        final PaginatedScanList<Campus> queryResultPage;

        try {
            if( lastEvaluatedKey != null ) {
                map.put(":id", new AttributeValue().withS(lastEvaluatedKey));
                paginatedExpression.setExclusiveStartKey(map);
            }
            queryResultPage = mapper.scan(Campus.class,paginatedExpression);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }

        return queryResultPage.subList(0,queryResultPage.size() > pageSize ? pageSize : queryResultPage.size());
    }

    @Override
    public void update(Campus campus) throws BackendException {
        try {
            mapper.save(campus);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
    }

    @Override
    public void delete(Campus campus) throws BackendException {
        try {
            mapper.delete(campus);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
    }
}
