package co.tpg.workflow.dao;

import co.tpg.workflow.dao.exception.BackendException;
import co.tpg.workflow.function.model.StepField;
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
 * DAO class persists workflow step field in the dynamoDB
 * @author Andrej
 * @since 2019-10-11
 */
public class StepFieldDAO implements DAO<StepField, String> {

    private static final String DYNAMO_TABLE_NAME = "StepField";
    private static final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
    private static final DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

    /**
     * Method returns the list of workflow step fields
     * @param key   Workflow step parent ID
     * @return      List of workflow step fields
     * @throws BackendException
     */
    public List<StepField> retrieveDependant(String key) throws BackendException {

        List<StepField> stepFields;

        try {

            // Define query parameters
            Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
            eav.put(":val1", new AttributeValue().withS(key.toString()));

            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                    .withFilterExpression("stepId = :val1")
                    .withExpressionAttributeValues(eav);

            stepFields = mapper.scan(StepField.class, scanExpression);

        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
        return stepFields;
    }

    @Override
    public StepField create(StepField stepField) throws BackendException {
        try {
            stepField.setId(UUID.randomUUID().toString());
            mapper.save(stepField);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
        return stepField;
    }

    @Override
    public StepField retrieveById(String key) throws BackendException {
        final StepField stepField;

        try {
            stepField = mapper.load(StepField.class,key);
            if ( stepField != null ) {

            }
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
        return stepField;
    }

    @Override
    public void update(StepField stepField) throws BackendException {
        try {
            mapper.save(stepField);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
    }

    @Override
    public void delete(StepField stepField) throws BackendException {
        try {
            mapper.delete(stepField);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
    }

    @Override
    public List<StepField> retrieveAll(String lastEvaluatedKey, int pageSize) throws BackendException {

        final Map<String, AttributeValue> map = new HashMap<>();
        final DynamoDBScanExpression paginatedExpression = new DynamoDBScanExpression()
                .withLimit(pageSize);
        final PaginatedScanList<StepField> queryResultPage;

        try {
            if( lastEvaluatedKey != null ) {
                map.put(":id", new AttributeValue().withS(String.valueOf(lastEvaluatedKey)));
                paginatedExpression.setExclusiveStartKey(map);
            }
            queryResultPage = mapper.scan(StepField.class,paginatedExpression);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }

        return queryResultPage.subList(0,queryResultPage.size() > pageSize ? pageSize : queryResultPage.size());
    }
}
