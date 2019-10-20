package co.tpg.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractFunction<I,O> implements RequestStreamHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        I request = objectMapper.readValue(inputStream,getClazz());
        O response = handleRequest(request, context);
        objectMapper.writeValue(outputStream,response);
    }

    public abstract O handleRequest(final I input, final Context context);

    public abstract Class<I> getClazz();
}
