package co.tpg.catalog.dao;

import co.tpg.catalog.dao.exception.BackendException;
import co.tpg.catalog.model.Address;
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

public class AddressDAO implements DAO<Address, String>{
    private static final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
    private static final String DYNAMO_TABLE_NAME = "Address";
    private static final DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

    @Override
    public Address create(Address address) throws BackendException {
        try {
            address.setId(UUID.randomUUID().toString());
            mapper.save(address);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(
                    String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
        return address;
    }

    @Override
    public Address retrieveById(String key) throws BackendException {
        final Address address;

        try {
            address = mapper.load(Address.class, key);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(
                    String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }

        return address;
    }

    @Override
    public List<Address> retrieveAll(String lastEvaluatedKey, int pageSize) throws BackendException {
        final Map<String, AttributeValue> map = new HashMap<>();
        final DynamoDBScanExpression paginatedExpression = new DynamoDBScanExpression().withLimit(pageSize);
        final PaginatedScanList<Address> queryResultPage;

        try {
            if (lastEvaluatedKey != null) {
                map.put(":id", new AttributeValue().withS(lastEvaluatedKey));
                paginatedExpression.setExclusiveStartKey(map);
            }
            queryResultPage = mapper.scan(Address.class, paginatedExpression);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(
                    String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }

        return queryResultPage.subList(0, queryResultPage.size() > pageSize ? pageSize : queryResultPage.size());
    }

    public void update(Address address) throws BackendException {
        try {
            mapper.save(address);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(
                    String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
    }

    @Override
    public void delete(Address address) throws BackendException {
        try {
            mapper.delete(address);
        } catch (ResourceNotFoundException ex) {
            throw new BackendException(
                    String.format("The table named %s could not be found in the backend system.", DYNAMO_TABLE_NAME));
        } catch (AmazonServiceException ex) {
            throw new BackendException(ex.getMessage());
        }
    }

}
