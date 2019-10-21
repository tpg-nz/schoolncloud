package co.tpg.catalog.dao;

import co.tpg.catalog.dao.exception.BackendException;
import co.tpg.catalog.model.Subject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
//import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
//import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
//import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

/**
 * DAO class to persist Subject into the backend.
 * 
 * @author maxx
 * @since 2019-10-07
 */

public class SubjectDAO implements DAO<Subject, String> {
    private static final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
    private static final String DYNAMO_TABLE_NAME = "Subject";
    private static final DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

    @Override
    public Subject create(Subject subject) throws BackendException {
        try {
            subject.setId(UUID.randomUUID().toString());
            mapper.save(subject);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(
                    String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
        return subject;
    }

    @Override
    public Subject retrieveById(String key) throws BackendException {
        final Subject subject;

        try {
            subject = mapper.load(Subject.class, key);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(
                    String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }

        return subject;
    }

    @Override
    public List<Subject> retrieveAll(String lastEvaluatedKey, int pageSize) throws BackendException {
        final Map<String, AttributeValue> map = new HashMap<>();
        final DynamoDBScanExpression paginatedExpression = new DynamoDBScanExpression().withLimit(pageSize);
        final PaginatedScanList<Subject> queryResultPage;

        try {
            if (lastEvaluatedKey != null) {
                map.put(":id", new AttributeValue().withS(lastEvaluatedKey));
                paginatedExpression.setExclusiveStartKey(map);
            }
            queryResultPage = mapper.scan(Subject.class, paginatedExpression);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(
                    String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }

        return queryResultPage.subList(0, queryResultPage.size() > pageSize ? pageSize : queryResultPage.size());
    }

    @Override
    public void update(Subject subject) throws BackendException {
        try {
            mapper.save(subject);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(
                    String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
    }

    @Override
    public void delete(Subject subject) throws BackendException {
        try {
            mapper.delete(subject);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(
                    String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
    }
}
