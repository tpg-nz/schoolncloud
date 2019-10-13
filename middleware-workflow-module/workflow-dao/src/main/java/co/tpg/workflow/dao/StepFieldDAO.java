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
     * Returns the list of workflow step fields for workflow step
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

    /**
     * Creates new workflow step field
     * @param stepField Workflow step field
     * @return          Newly created workflow step field
     * @throws BackendException
     */
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

    /**
     * Retrieves workflow step field object using its id
     * @param key   Workflow step field id
     * @return      Workflow step field object reference
     * @throws BackendException
     */
    @Override
    public StepField retrieveById(String key) throws BackendException {
        final StepField stepField;

        try {
            stepField = mapper.load(StepField.class,key);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
        return stepField;
    }

    /**
     * Updates the workflow step field object
     * @param stepField Workflow step field
     * @throws BackendException
     */
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

    /**
     * Deletes the workflow step field object
     * @param stepField Workflow step field object
     * @throws BackendException
     */
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

    /**
     * Rerieves all workflow step fields using paginated result
     * @param lastEvaluatedKey  Last used workflow step field id
     * @param pageSize          Page size
     * @return                  List of workflow step field objects
     * @throws BackendException
     */
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
