package co.tpg.workflow.dao;

import co.tpg.workflow.dao.exception.BackendException;
import co.tpg.workflow.function.model.Step;
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
 * DAO class to persist workflow step into the dynamoDB.
 * @author Andrej
 * @since 2019-10-06
 */
public class StepDAO implements DAO<Step, String> {

    private static final String DYNAMO_TABLE_NAME = "Step";
    private static final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
    private static final DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
    private StepFieldDAO stepFieldDAO = new StepFieldDAO();

    @Override
    public Step create(Step step) throws BackendException {
        try {
            // set step id and create in DB
            step.setId(UUID.randomUUID().toString());
            mapper.save(step);

            // Create step dependant nodes
            List<StepField> stepFields = step.getSteps();
            if ( stepFields != null ) {
                for (StepField stepField: stepFields) {
                    stepField.setStep(step);
                    stepFieldDAO.create(stepField);
                }
            }
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
        return step;
    }

    @Override
    public Step retrieveById(String key) throws BackendException {
        final Step step;

        try {
            step = mapper.load(Step.class,key);
            // Load workflow step fields dependant nodes
            List<StepField> stepFields = stepFieldDAO.retrieveDependant(step.getId());
            if (stepFields != null) {
                step.setStepFields( stepFields);
            }
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
        return step;
    }

    @Override
    public void update(Step step) throws BackendException {
        try {
            // Update step
            mapper.save(step);

            // Update step fields
            List<StepField> stepFields = step.getSteps();
            if ( stepFields != null ) {
                for (StepField stepField: stepFields) {
                    stepFieldDAO.update(stepField);
                }
            }
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
    }

    @Override
    public void delete(Step step) throws BackendException {

        try {
            // Delete all step fields first
            List<StepField> stepFields = step.getSteps();
            if ( ( stepFields != null ) && (stepFields.size() > 0)) {
                for (StepField stepField: stepFields) {
                    stepFieldDAO.delete(stepField);
                }
            }
            // Delete step
            mapper.delete(step);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
    }

    @Override
    public List<Step> retrieveAll(String lastEvaluatedKey, int pageSize) throws BackendException {

        final Map<String, AttributeValue> map = new HashMap<>();
        final DynamoDBScanExpression paginatedExpression = new DynamoDBScanExpression()
                .withLimit(pageSize);
        final PaginatedScanList<Step> queryResultPage;

        try {
            if( lastEvaluatedKey != null ) {
                map.put(":id", new AttributeValue().withS(String.valueOf(lastEvaluatedKey)));
                paginatedExpression.setExclusiveStartKey(map);
            }
            // Set the result page with dependant nodes
            queryResultPage = mapper.scan(Step.class,paginatedExpression);

            // Update the result content for dependant nodes
            queryResultPage.forEach(step -> {
                try {
                    step.setSteps(stepFieldDAO.retrieveDependant(step.getId()));
                } catch (BackendException e) {
                    e.printStackTrace();
                }
            });

        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }

        return queryResultPage.subList(0,queryResultPage.size() > pageSize ? pageSize : queryResultPage.size());
    }

    /**
     * Method returns the list of workflow step fields
     * @param key   Workflow step parent ID
     * @return      List of workflow step fields
     * @throws BackendException
     */
    public List<Step> retrieveDependant(String key) throws BackendException {

        List<Step> steps;

        try {
            // Define query parameters
            Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
            eav.put(":val1", new AttributeValue().withS(key.toString()));

            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                    .withFilterExpression("workflowId = :val1")
                    .withExpressionAttributeValues(eav);
            // scan DB
            steps = mapper.scan(Step.class, scanExpression);

            if ((steps != null) && (steps.size() > 0) ){
                // retrieve dependant step field nodes
                for (Step step: steps ) {
                    step.setStepFields(stepFieldDAO.retrieveDependant(step.getId()));
                }
            }
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
        return steps;
    }
}
