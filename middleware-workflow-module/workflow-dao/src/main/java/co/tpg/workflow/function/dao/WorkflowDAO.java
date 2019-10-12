package co.tpg.workflow.function.dao;

import co.tpg.workflow.function.dao.exception.BackendException;
import co.tpg.workflow.function.model.Step;
import co.tpg.workflow.function.model.Workflow;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import java.util.*;

/**
 * DAO class persists workflow in the dynamoDB
 * @author Andrej
 * @since 2019-10-11
 */
public class WorkflowDAO implements DAO<Workflow, String> {

    private static final String DYNAMO_TABLE_NAME = "Workflow";
    private static final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
    private static final DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

    private StepDAO stepDAO = new StepDAO();

    @Override
    public Workflow create(Workflow workflow) throws BackendException {

        try {
            // Create workflow
            workflow.setId(UUID.randomUUID().toString());
            mapper.save(workflow);

            // Create dependant objects
            ArrayList<Step> steps = workflow.getSteps();
            if ( steps != null ) {
                for (Step step: steps) {
                    stepDAO.create(step);
                }
            }
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
        return workflow;
    }

    @Override
    public Workflow retrieveById(String key) throws BackendException {
        final Workflow workflow;

        try {
            // Load workflow
            workflow = mapper.load(Workflow.class,key);
            // Load workflow dependant objects
            workflow.setSteps( (ArrayList<Step>) stepDAO.retrieveDependant(workflow.getId()));
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
        return workflow;
    }

    @Override
    public void update(Workflow workflow) throws BackendException {

        try {
            // Update workflow
            mapper.save(workflow);
            // Update steps
            ArrayList<Step> steps = workflow.getSteps();
            if ( steps != null ) {
                steps.forEach(step-> {
                    try {
                        stepDAO.update(step);
                    } catch (BackendException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
    }

    @Override
    public void delete(Workflow workflow) throws BackendException {

        try {
            // Delete all steps first
            ArrayList<Step> steps = workflow.getSteps();
            if (steps != null ) {
                steps.forEach(step -> {
                    try {
                        stepDAO.delete(step);
                    } catch (BackendException e) {
                        e.printStackTrace();
                    }
                });
            }
            // Delete the workflow
            mapper.delete(workflow);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
    }

    @Override
    public List<Workflow> retrieveAll(String lastEvaluatedKey, int pageSize) throws BackendException {

        final Map<String, AttributeValue> map = new HashMap<>();
        final DynamoDBScanExpression paginatedExpression = new DynamoDBScanExpression()
                .withLimit(pageSize);
        final PaginatedScanList<Workflow> queryResultPage;

        try {
            if( lastEvaluatedKey != null ) {
                map.put(":id", new AttributeValue().withS(lastEvaluatedKey));
                paginatedExpression.setExclusiveStartKey(map);
            }
            // Set the result page
            queryResultPage = mapper.scan(Workflow.class,paginatedExpression);

            // Update teh result content for dependant nodes

        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
        return queryResultPage.subList(0,queryResultPage.size() > pageSize ? pageSize : queryResultPage.size());
    }
}
