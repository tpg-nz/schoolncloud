package co.tpg.catalog.dao;

import co.tpg.catalog.dao.exception.BackendException;
import co.tpg.catalog.model.TeachingClass;
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
import java.util.stream.Collectors;

/**
 * DAO class to persist TeachingClass into the backend.
 * @author Rod
 * @since 2019-10-06
 */
public class TeachingClassDAO implements DAO<TeachingClass,String>{
    private static final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
    private static final String DYNAMO_TABLE_NAME = "TeachingClass";
    private static final DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

    @Override
    public TeachingClass create(TeachingClass teachingClass) throws BackendException {
        try {
            teachingClass.setId(UUID.randomUUID().toString());
            mapper.save(teachingClass);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
        return teachingClass;
    }

    @Override
    public TeachingClass retrieveById(String key) throws BackendException {
        final TeachingClass teachingClass;
        final CampusDAO campusDAO = new CampusDAO();
        final PaperDAO paperDAO = new PaperDAO();

        try {
            teachingClass = mapper.load(TeachingClass.class,key);
            teachingClass.setCampus(campusDAO.retrieveById(teachingClass.getCampusId()));
            teachingClass.setPaper(paperDAO.retrieveById(teachingClass.getPaperId()));
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }

        return teachingClass;
    }

    @Override
    public List<TeachingClass> retrieveAll(String lastEvaluatedKey, int pageSize) throws BackendException {
        final Map<String, AttributeValue> map = new HashMap<>();
        final DynamoDBScanExpression paginatedExpression = new DynamoDBScanExpression()
                .withLimit(pageSize);
        final PaginatedScanList<TeachingClass> queryResultPage;
        final CampusDAO campusDAO = new CampusDAO();
        final PaperDAO paperDAO = new PaperDAO();

        try {
            if( lastEvaluatedKey != null ) {
                map.put(":id", new AttributeValue().withS(lastEvaluatedKey));
                paginatedExpression.setExclusiveStartKey(map);
            }
            queryResultPage = mapper.scan(TeachingClass.class,paginatedExpression);
            queryResultPage
                    .parallelStream()
                    .map(ThrowingFunction.unchecked(o -> {
                            o.setPaper(paperDAO.retrieveById(o.getPaperId()));
                            o.setCampus(campusDAO.retrieveById(o.getCampusId()));
                        return o;
                    }))
                    .collect(Collectors.toList());
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }

        return queryResultPage.subList(0,queryResultPage.size() > pageSize ? pageSize : queryResultPage.size());
    }

    @Override
    public void update(TeachingClass teachingClass) throws BackendException {
        try {
            mapper.save(teachingClass);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
    }

    @Override
    public void delete(TeachingClass teachingClass) throws BackendException {
        try {
            mapper.delete(teachingClass);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
    }
}
