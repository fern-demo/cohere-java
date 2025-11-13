package com.cohere.api;

import com.cohere.api.core.ObjectMappers;
import com.cohere.api.resources.embedjobs.requests.CreateEmbedJobRequest;
import com.cohere.api.types.CreateEmbedJobResponse;
import com.cohere.api.types.EmbedInputType;
import com.cohere.api.types.EmbedJob;
import com.cohere.api.types.ListEmbedJobResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmbedJobsWireTest {
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
                                "{\"embed_jobs\":[{\"job_id\":\"job_id\",\"name\":\"name\",\"status\":\"processing\",\"created_at\":\"2024-01-15T09:30:00Z\",\"input_dataset_id\":\"input_dataset_id\",\"output_dataset_id\":\"output_dataset_id\",\"model\":\"model\",\"truncate\":\"START\"}]}"));
        ListEmbedJobResponse response = client.embedJobs().list();
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"embed_jobs\": [\n"
                + "    {\n"
                + "      \"job_id\": \"job_id\",\n"
                + "      \"name\": \"name\",\n"
                + "      \"status\": \"processing\",\n"
                + "      \"created_at\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"input_dataset_id\": \"input_dataset_id\",\n"
                + "      \"output_dataset_id\": \"output_dataset_id\",\n"
                + "      \"model\": \"model\",\n"
                + "      \"truncate\": \"START\"\n"
                + "    }\n"
                + "  ]\n"
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
                                "{\"job_id\":\"job_id\",\"meta\":{\"api_version\":{\"version\":\"version\",\"is_deprecated\":true,\"is_experimental\":true},\"billed_units\":{\"images\":1.1,\"input_tokens\":1.1,\"output_tokens\":1.1,\"search_units\":1.1,\"classifications\":1.1},\"tokens\":{\"input_tokens\":1.1,\"output_tokens\":1.1},\"cached_tokens\":1.1,\"warnings\":[\"warnings\"]}}"));
        CreateEmbedJobResponse response = client.embedJobs()
                .create(CreateEmbedJobRequest.builder()
                        .model("model")
                        .datasetId("dataset_id")
                        .inputType(EmbedInputType.SEARCH_DOCUMENT)
                        .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"model\": \"model\",\n"
                + "  \"dataset_id\": \"dataset_id\",\n"
                + "  \"input_type\": \"search_document\"\n"
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
                + "  \"job_id\": \"job_id\",\n"
                + "  \"meta\": {\n"
                + "    \"api_version\": {\n"
                + "      \"version\": \"version\",\n"
                + "      \"is_deprecated\": true,\n"
                + "      \"is_experimental\": true\n"
                + "    },\n"
                + "    \"billed_units\": {\n"
                + "      \"images\": 1.1,\n"
                + "      \"input_tokens\": 1.1,\n"
                + "      \"output_tokens\": 1.1,\n"
                + "      \"search_units\": 1.1,\n"
                + "      \"classifications\": 1.1\n"
                + "    },\n"
                + "    \"tokens\": {\n"
                + "      \"input_tokens\": 1.1,\n"
                + "      \"output_tokens\": 1.1\n"
                + "    },\n"
                + "    \"cached_tokens\": 1.1,\n"
                + "    \"warnings\": [\n"
                + "      \"warnings\"\n"
                + "    ]\n"
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
    public void testGet() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"job_id\":\"job_id\",\"name\":\"name\",\"status\":\"processing\",\"created_at\":\"2024-01-15T09:30:00Z\",\"input_dataset_id\":\"input_dataset_id\",\"output_dataset_id\":\"output_dataset_id\",\"model\":\"model\",\"truncate\":\"START\",\"meta\":{\"api_version\":{\"version\":\"version\",\"is_deprecated\":true,\"is_experimental\":true},\"billed_units\":{\"images\":1.1,\"input_tokens\":1.1,\"output_tokens\":1.1,\"search_units\":1.1,\"classifications\":1.1},\"tokens\":{\"input_tokens\":1.1,\"output_tokens\":1.1},\"cached_tokens\":1.1,\"warnings\":[\"warnings\"]}}"));
        EmbedJob response = client.embedJobs().get("id");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"job_id\": \"job_id\",\n"
                + "  \"name\": \"name\",\n"
                + "  \"status\": \"processing\",\n"
                + "  \"created_at\": \"2024-01-15T09:30:00Z\",\n"
                + "  \"input_dataset_id\": \"input_dataset_id\",\n"
                + "  \"output_dataset_id\": \"output_dataset_id\",\n"
                + "  \"model\": \"model\",\n"
                + "  \"truncate\": \"START\",\n"
                + "  \"meta\": {\n"
                + "    \"api_version\": {\n"
                + "      \"version\": \"version\",\n"
                + "      \"is_deprecated\": true,\n"
                + "      \"is_experimental\": true\n"
                + "    },\n"
                + "    \"billed_units\": {\n"
                + "      \"images\": 1.1,\n"
                + "      \"input_tokens\": 1.1,\n"
                + "      \"output_tokens\": 1.1,\n"
                + "      \"search_units\": 1.1,\n"
                + "      \"classifications\": 1.1\n"
                + "    },\n"
                + "    \"tokens\": {\n"
                + "      \"input_tokens\": 1.1,\n"
                + "      \"output_tokens\": 1.1\n"
                + "    },\n"
                + "    \"cached_tokens\": 1.1,\n"
                + "    \"warnings\": [\n"
                + "      \"warnings\"\n"
                + "    ]\n"
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
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{}"));
        client.embedJobs().cancel("id");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
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
