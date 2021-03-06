package io.chaotics.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chaotics.api.ApiSecretPair;
import io.chaotics.api.CreateApiRequest;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;

import java.io.IOException;

public class ChaoticsClient {

    private static final String CHAOTICS_URI = "https://api.chaotics.io/endpoint";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String chaoticsUrl;

    public ChaoticsClient() {
        this.chaoticsUrl = CHAOTICS_URI;
    }

    public ApiSecretPair createEndpoint(CreateApiRequest createApiRequest) throws IOException {
        String response = Request.Post(CHAOTICS_URI)
                .bodyString(objectMapper.writeValueAsString(createApiRequest), ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();
        return objectMapper.readValue(response, ApiSecretPair.class);
    }

    public Response getResponse(String id) throws IOException {
        return Request.Get(CHAOTICS_URI + "/" + id)
                .execute();
    }

    public void deleteEndpoint(ApiSecretPair apiSecretPair) throws IOException {
        Request.Delete(CHAOTICS_URI + "/" + apiSecretPair.getApiId())
            .addHeader("Authorization", apiSecretPair.getSecret())
            .execute()
            .returnContent()
            .asString();
    }

    public void overrideUrl(String chaoticsUrl) {
        this.chaoticsUrl = chaoticsUrl;
    }
}
