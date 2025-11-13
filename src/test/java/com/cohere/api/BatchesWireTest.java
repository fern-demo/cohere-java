package com.cohere.api;

import com.cohere.api.core.ObjectMappers;
import com.cohere.api.resources.batches.requests.BatchesListBatchesRequest;
import com.cohere.api.resources.batches.types.Batch;
import com.cohere.api.resources.batches.types.CreateBatchResponse;
import com.cohere.api.resources.batches.types.GetBatchResponse;
import com.cohere.api.resources.batches.types.ListBatchesResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BatchesWireTest {
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
    public void testList() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"batches\":[{\"id\":\"9439e0e3-49a4-4336-93e3-485fc420bc18\",\"name\":\"my-batch\",\"creator_id\":\"b5e1ffb9-49a1-4653-9927-94083316309d\",\"org_id\":\"62603116-1750-4c04-b1b6-9d5046764bc4\",\"status\":\"BATCH_STATUS_IN_PROGRESS\",\"created_at\":\"2024-01-15T09:30:00Z\",\"updated_at\":\"2024-01-15T09:30:00Z\",\"input_dataset_id\":\"my-requests-dataset\",\"output_dataset_id\":\"output_dataset_id\",\"input_tokens\":\"input_tokens\",\"output_tokens\":\"output_tokens\",\"model\":\"command\",\"num_records\":5000,\"num_successful_records\":1,\"num_failed_records\":1,\"status_reason\":\"status_reason\"}],\"next_page_token\":\"next_page_token\"}"));
        ListBatchesResponse response = client.batches()
                .list(BatchesListBatchesRequest.builder()
                        .pageSize(1)
                        .pageToken("page_token")
                        .orderBy("order_by")
                        .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"batches\": [\n"
                + "    {\n"
                + "      \"id\": \"9439e0e3-49a4-4336-93e3-485fc420bc18\",\n"
                + "      \"name\": \"my-batch\",\n"
                + "      \"creator_id\": \"b5e1ffb9-49a1-4653-9927-94083316309d\",\n"
                + "      \"org_id\": \"62603116-1750-4c04-b1b6-9d5046764bc4\",\n"
                + "      \"status\": \"BATCH_STATUS_IN_PROGRESS\",\n"
                + "      \"created_at\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"updated_at\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"input_dataset_id\": \"my-requests-dataset\",\n"
                + "      \"output_dataset_id\": \"output_dataset_id\",\n"
                + "      \"input_tokens\": \"input_tokens\",\n"
                + "      \"output_tokens\": \"output_tokens\",\n"
                + "      \"model\": \"command\",\n"
                + "      \"num_records\": 5000,\n"
                + "      \"num_successful_records\": 1,\n"
                + "      \"num_failed_records\": 1,\n"
                + "      \"status_reason\": \"status_reason\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"next_page_token\": \"next_page_token\"\n"
                + "}";
        JsonNode actualResponseNode = objectMapper.readTree(actualResponseJson);
        JsonNode expectedResponseNode = objectMapper.readTree(expectedResponseBody);
        Assertions.assertTrue(
                jsonEquals(expectedResponseNode, actualResponseNode),
                "Response body structure does not match expected");
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
    public void testCreate() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"batch\":{\"id\":\"id\",\"name\":\"name\",\"creator_id\":\"creator_id\",\"org_id\":\"org_id\",\"status\":\"BATCH_STATUS_UNSPECIFIED\",\"created_at\":\"2024-01-15T09:30:00Z\",\"updated_at\":\"2024-01-15T09:30:00Z\",\"input_dataset_id\":\"input_dataset_id\",\"output_dataset_id\":\"output_dataset_id\",\"input_tokens\":\"input_tokens\",\"output_tokens\":\"output_tokens\",\"model\":\"model\",\"num_records\":1,\"num_successful_records\":1,\"num_failed_records\":1,\"status_reason\":\"status_reason\"}}"));
        CreateBatchResponse response = client.batches()
                .create(Batch.builder()
                        .name("name")
                        .inputDatasetId("input_dataset_id")
                        .model("model")
                        .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"name\": \"name\",\n"
                + "  \"input_dataset_id\": \"input_dataset_id\",\n"
                + "  \"model\": \"model\"\n"
                + "}";
        JsonNode actualJson = objectMapper.readTree(actualRequestBody);
        JsonNode expectedJson = objectMapper.readTree(expectedRequestBody);
        Assertions.assertTrue(jsonEquals(expectedJson, actualJson), "Request body structure does not match expected");
        if (actualJson.has("type") || actualJson.has("_type") || actualJson.has("kind")) {
            String discriminator = null;
            if (actualJson.has("type")) discriminator = actualJson.get("type").asText();
            else if (actualJson.has("_type"))
                discriminator = actualJson.get("_type").asText();
            else if (actualJson.has("kind"))
                discriminator = actualJson.get("kind").asText();
            Assertions.assertNotNull(discriminator, "Union type should have a discriminator field");
            Assertions.assertFalse(discriminator.isEmpty(), "Union discriminator should not be empty");
        }

        if (!actualJson.isNull()) {
            Assertions.assertTrue(
                    actualJson.isObject() || actualJson.isArray() || actualJson.isValueNode(),
                    "request should be a valid JSON value");
        }

        if (actualJson.isArray()) {
            Assertions.assertTrue(actualJson.size() >= 0, "Array should have valid size");
        }
        if (actualJson.isObject()) {
            Assertions.assertTrue(actualJson.size() >= 0, "Object should have valid field count");
        }

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"batch\": {\n"
                + "    \"id\": \"id\",\n"
                + "    \"name\": \"name\",\n"
                + "    \"creator_id\": \"creator_id\",\n"
                + "    \"org_id\": \"org_id\",\n"
                + "    \"status\": \"BATCH_STATUS_UNSPECIFIED\",\n"
                + "    \"created_at\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"updated_at\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"input_dataset_id\": \"input_dataset_id\",\n"
                + "    \"output_dataset_id\": \"output_dataset_id\",\n"
                + "    \"input_tokens\": \"input_tokens\",\n"
                + "    \"output_tokens\": \"output_tokens\",\n"
                + "    \"model\": \"model\",\n"
                + "    \"num_records\": 1,\n"
                + "    \"num_successful_records\": 1,\n"
                + "    \"num_failed_records\": 1,\n"
                + "    \"status_reason\": \"status_reason\"\n"
                + "  }\n"
                + "}";
        JsonNode actualResponseNode = objectMapper.readTree(actualResponseJson);
        JsonNode expectedResponseNode = objectMapper.readTree(expectedResponseBody);
        Assertions.assertTrue(
                jsonEquals(expectedResponseNode, actualResponseNode),
                "Response body structure does not match expected");
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
    public void testRetrieve() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"batch\":{\"id\":\"9439e0e3-49a4-4336-93e3-485fc420bc18\",\"name\":\"my-batch\",\"creator_id\":\"b5e1ffb9-49a1-4653-9927-94083316309d\",\"org_id\":\"62603116-1750-4c04-b1b6-9d5046764bc4\",\"status\":\"BATCH_STATUS_IN_PROGRESS\",\"created_at\":\"2025-03-05T14:59:25Z\",\"updated_at\":\"2025-03-05T15:00:26Z\",\"input_dataset_id\":\"my-requests-dataset\",\"output_dataset_id\":\"my-batch-output-c2waz1\",\"input_tokens\":\"0\",\"output_tokens\":\"0\",\"model\":\"batch-command\",\"num_records\":5000,\"num_successful_records\":8,\"num_failed_records\":0,\"status_reason\":\"status_reason\"}}"));
        GetBatchResponse response = client.batches().retrieve("id");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"batch\": {\n"
                + "    \"id\": \"9439e0e3-49a4-4336-93e3-485fc420bc18\",\n"
                + "    \"name\": \"my-batch\",\n"
                + "    \"creator_id\": \"b5e1ffb9-49a1-4653-9927-94083316309d\",\n"
                + "    \"org_id\": \"62603116-1750-4c04-b1b6-9d5046764bc4\",\n"
                + "    \"status\": \"BATCH_STATUS_IN_PROGRESS\",\n"
                + "    \"created_at\": \"2025-03-05T14:59:25Z\",\n"
                + "    \"updated_at\": \"2025-03-05T15:00:26Z\",\n"
                + "    \"input_dataset_id\": \"my-requests-dataset\",\n"
                + "    \"output_dataset_id\": \"my-batch-output-c2waz1\",\n"
                + "    \"input_tokens\": \"0\",\n"
                + "    \"output_tokens\": \"0\",\n"
                + "    \"model\": \"batch-command\",\n"
                + "    \"num_records\": 5000,\n"
                + "    \"num_successful_records\": 8,\n"
                + "    \"num_failed_records\": 0,\n"
                + "    \"status_reason\": \"status_reason\"\n"
                + "  }\n"
                + "}";
        JsonNode actualResponseNode = objectMapper.readTree(actualResponseJson);
        JsonNode expectedResponseNode = objectMapper.readTree(expectedResponseBody);
        Assertions.assertTrue(
                jsonEquals(expectedResponseNode, actualResponseNode),
                "Response body structure does not match expected");
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
    public void testCancel() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{\"key\":\"value\"}"));
        Map<String, Object> response = client.batches().cancel("id");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = "" + "{\n" + "  \"key\": \"value\"\n" + "}";
        JsonNode actualResponseNode = objectMapper.readTree(actualResponseJson);
        JsonNode expectedResponseNode = objectMapper.readTree(expectedResponseBody);
        Assertions.assertTrue(
                jsonEquals(expectedResponseNode, actualResponseNode),
                "Response body structure does not match expected");
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

    /**
     * Compares two JsonNodes with numeric equivalence.
     */
    private boolean jsonEquals(JsonNode a, JsonNode b) {
        if (a.equals(b)) return true;
        if (a.isNumber() && b.isNumber()) return Math.abs(a.doubleValue() - b.doubleValue()) < 1e-10;
        if (a.isObject() && b.isObject()) {
            if (a.size() != b.size()) return false;
            java.util.Iterator<java.util.Map.Entry<String, JsonNode>> iter = a.fields();
            while (iter.hasNext()) {
                java.util.Map.Entry<String, JsonNode> entry = iter.next();
                if (!jsonEquals(entry.getValue(), b.get(entry.getKey()))) return false;
            }
            return true;
        }
        if (a.isArray() && b.isArray()) {
            if (a.size() != b.size()) return false;
            for (int i = 0; i < a.size(); i++) {
                if (!jsonEquals(a.get(i), b.get(i))) return false;
            }
            return true;
        }
        return false;
    }
}
