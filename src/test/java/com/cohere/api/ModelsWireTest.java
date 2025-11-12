package com.cohere.api;

import com.cohere.api.core.ObjectMappers;
import com.cohere.api.resources.models.requests.ModelsListRequest;
import com.cohere.api.types.CompatibleEndpoint;
import com.cohere.api.types.GetModelResponse;
import com.cohere.api.types.ListModelsResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ModelsWireTest {
    private MockWebServer server;
    private Cohere client;
    private ObjectMapper objectMapper = ObjectMappers.JSON_MAPPER;

    @BeforeEach
    public void setup() throws Exception {
        server = new MockWebServer();
        server.start();
        client = Cohere.builder()
                .url(server.url("/").toString())
                .token("test-token")
                .build();
    }

    @AfterEach
    public void teardown() throws Exception {
        server.shutdown();
    }

    @Test
    public void testGet() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"name\":\"name\",\"is_deprecated\":true,\"endpoints\":[\"chat\"],\"finetuned\":true,\"context_length\":1.1,\"tokenizer_url\":\"tokenizer_url\",\"default_endpoints\":[\"chat\"],\"features\":[\"features\"]}"));
        GetModelResponse response = client.models().get("command-a-03-2025");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"name\": \"name\",\n"
                + "  \"is_deprecated\": true,\n"
                + "  \"endpoints\": [\n"
                + "    \"chat\"\n"
                + "  ],\n"
                + "  \"finetuned\": true,\n"
                + "  \"context_length\": 1.1,\n"
                + "  \"tokenizer_url\": \"tokenizer_url\",\n"
                + "  \"default_endpoints\": [\n"
                + "    \"chat\"\n"
                + "  ],\n"
                + "  \"features\": [\n"
                + "    \"features\"\n"
                + "  ]\n"
                + "}";
        JsonNode actualResponseNode = objectMapper.readTree(actualResponseJson);
        JsonNode expectedResponseNode = objectMapper.readTree(expectedResponseBody);
        Assertions.assertEquals(
                expectedResponseNode, actualResponseNode, "Response body structure does not match expected");
        if (actualResponseNode.has("type") || actualResponseNode.has("_type") || actualResponseNode.has("kind")) {
            String discriminator = null;
            if (actualResponseNode.has("type"))
                discriminator = actualResponseNode.get("type").asText();
            else if (actualResponseNode.has("_type"))
                discriminator = actualResponseNode.get("_type").asText();
            else if (actualResponseNode.has("kind"))
                discriminator = actualResponseNode.get("kind").asText();
            Assertions.assertNotNull(discriminator, "Union type should have a discriminator field");
            Assertions.assertFalse(discriminator.isEmpty(), "Union discriminator should not be empty");
        }

        if (!actualResponseNode.isNull()) {
            Assertions.assertTrue(
                    actualResponseNode.isObject() || actualResponseNode.isArray() || actualResponseNode.isValueNode(),
                    "response should be a valid JSON value");
        }

        if (actualResponseNode.isArray()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Array should have valid size");
        }
        if (actualResponseNode.isObject()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Object should have valid field count");
        }
    }

    @Test
    public void testList() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"models\":[{\"name\":\"name\",\"is_deprecated\":true,\"endpoints\":[\"chat\"],\"finetuned\":true,\"context_length\":1.1,\"tokenizer_url\":\"tokenizer_url\",\"default_endpoints\":[\"chat\"],\"features\":[\"features\"]}],\"next_page_token\":\"next_page_token\"}"));
        ListModelsResponse response = client.models()
                .list(ModelsListRequest.builder()
                        .pageSize(1.1)
                        .pageToken("page_token")
                        .endpoint(CompatibleEndpoint.CHAT)
                        .defaultOnly(true)
                        .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"models\": [\n"
                + "    {\n"
                + "      \"name\": \"name\",\n"
                + "      \"is_deprecated\": true,\n"
                + "      \"endpoints\": [\n"
                + "        \"chat\"\n"
                + "      ],\n"
                + "      \"finetuned\": true,\n"
                + "      \"context_length\": 1.1,\n"
                + "      \"tokenizer_url\": \"tokenizer_url\",\n"
                + "      \"default_endpoints\": [\n"
                + "        \"chat\"\n"
                + "      ],\n"
                + "      \"features\": [\n"
                + "        \"features\"\n"
                + "      ]\n"
                + "    }\n"
                + "  ],\n"
                + "  \"next_page_token\": \"next_page_token\"\n"
                + "}";
        JsonNode actualResponseNode = objectMapper.readTree(actualResponseJson);
        JsonNode expectedResponseNode = objectMapper.readTree(expectedResponseBody);
        Assertions.assertEquals(
                expectedResponseNode, actualResponseNode, "Response body structure does not match expected");
        if (actualResponseNode.has("type") || actualResponseNode.has("_type") || actualResponseNode.has("kind")) {
            String discriminator = null;
            if (actualResponseNode.has("type"))
                discriminator = actualResponseNode.get("type").asText();
            else if (actualResponseNode.has("_type"))
                discriminator = actualResponseNode.get("_type").asText();
            else if (actualResponseNode.has("kind"))
                discriminator = actualResponseNode.get("kind").asText();
            Assertions.assertNotNull(discriminator, "Union type should have a discriminator field");
            Assertions.assertFalse(discriminator.isEmpty(), "Union discriminator should not be empty");
        }

        if (!actualResponseNode.isNull()) {
            Assertions.assertTrue(
                    actualResponseNode.isObject() || actualResponseNode.isArray() || actualResponseNode.isValueNode(),
                    "response should be a valid JSON value");
        }

        if (actualResponseNode.isArray()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Array should have valid size");
        }
        if (actualResponseNode.isObject()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Object should have valid field count");
        }
    }
}
