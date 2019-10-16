package co.tpg.workflow.dao;

import co.tpg.workflow.dao.exception.BackendException;
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

    /**
     * Creates new workflow
     * @param workflow  Workflow
     * @return  Newly created workflow object
     * @throws BackendException
     */
    @Override
    public Workflow create(Workflow workflow) throws BackendException {

        try {
            // Create workflow
            workflow.setId(UUID.randomUUID().toString());
            mapper.save(workflow);

            // Create dependant objects
            List<Step> steps = workflow.getSteps();
            if (( steps != null ) && (steps.size() > 0)){
                for (Step step: steps) {
                    step.setWorkflow(workflow);
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

    /**
     * Retrieves workflow object using its id
     * @param key   Workflow id
     * @return      Workflow object reference
     * @throws BackendException
     */
    @Override
    public Workflow retrieveById(String key) throws BackendException {
        final Workflow workflow;

        try {
            // Load workflow
            workflow = mapper.load(Workflow.class,key);
            // Load workflow dependant objects
            List<Step> steps = stepDAO.retrieveDependant(workflow.getId());
            if ( (steps != null) && (steps.size() > 0)  ){
                workflow.setSteps( steps );
            }
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
        return workflow;
    }

    /**
     * Updates the workflow object
     * @param workflow  Workflow object
     * @throws BackendException
     */
    @Override
    public void update(Workflow workflow) throws BackendException {

        try {
            // Update workflow
            mapper.save(workflow);
            // Update steps
            List<Step> steps = workflow.getSteps();
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

    /**
     * Deletes the workflow object
     * @param workflow  Workflow object
     * @throws BackendException
     */
    @Override
    public void delete(Workflow workflow) throws BackendException {

        try {
            // Delete all steps first
            List<Step> steps = workflow.getSteps();
            if ( (steps != null ) && (steps.size() > 0) ) {
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

    /**
     * Retrieve all workflow using paginated approach
     * @param lastEvaluatedKey  Last used workflow id
     * @param pageSize          Page size
     * @return                  List of workflow objects
     * @throws BackendException
     */
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

            if ((queryResultPage != null) && (queryResultPage.size() > 0) ){
                // retrieve dependant step field nodes
                for (Workflow workflow: queryResultPage ) {
                    workflow.setSteps(stepDAO.retrieveDependant(workflow.getId()));
                }
            }
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
        return queryResultPage.subList(0,queryResultPage.size() > pageSize ? pageSize : queryResultPage.size());
    }
}
