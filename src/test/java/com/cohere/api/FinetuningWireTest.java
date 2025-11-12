package com.cohere.api;

import com.cohere.api.core.ObjectMappers;
import com.cohere.api.resources.finetuning.finetuning.types.BaseModel;
import com.cohere.api.resources.finetuning.finetuning.types.BaseType;
import com.cohere.api.resources.finetuning.finetuning.types.CreateFinetunedModelResponse;
import com.cohere.api.resources.finetuning.finetuning.types.FinetunedModel;
import com.cohere.api.resources.finetuning.finetuning.types.GetFinetunedModelResponse;
import com.cohere.api.resources.finetuning.finetuning.types.ListEventsResponse;
import com.cohere.api.resources.finetuning.finetuning.types.ListFinetunedModelsResponse;
import com.cohere.api.resources.finetuning.finetuning.types.ListTrainingStepMetricsResponse;
import com.cohere.api.resources.finetuning.finetuning.types.Settings;
import com.cohere.api.resources.finetuning.finetuning.types.UpdateFinetunedModelResponse;
import com.cohere.api.resources.finetuning.requests.FinetuningListEventsRequest;
import com.cohere.api.resources.finetuning.requests.FinetuningListFinetunedModelsRequest;
import com.cohere.api.resources.finetuning.requests.FinetuningListTrainingStepMetricsRequest;
import com.cohere.api.resources.finetuning.requests.FinetuningUpdateFinetunedModelRequest;
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

public class FinetuningWireTest {
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
    public void testListFinetunedModels() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"finetuned_models\":[{\"id\":\"fee37446-7fc7-42f9-a026-c6ba2fcc422d\",\"name\":\"chat-ft\",\"creator_id\":\"7a317d97-4d05-427d-9396-f31b9fb92c55\",\"organization_id\":\"6bdca3d5-3eae-4de0-ac34-786d8063b7ee\",\"settings\":{\"base_model\":{\"name\":\"medium\",\"version\":\"14.2.0\",\"base_type\":\"BASE_TYPE_CHAT\",\"strategy\":\"STRATEGY_TFEW\"},\"dataset_id\":\"my-dataset-d701tr\",\"hyperparameters\":{\"early_stopping_patience\":6,\"early_stopping_threshold\":0.01,\"train_batch_size\":16,\"train_epochs\":1,\"learning_rate\":0.01}},\"status\":\"STATUS_READY\",\"created_at\":\"2024-01-17T20:11:42Z\",\"updated_at\":\"2024-01-17T20:31:06Z\",\"completed_at\":\"2024-01-17T20:31:05Z\",\"last_used\":\"2024-01-15T09:30:00Z\"},{\"id\":\"9d927c5e-7598-4772-98b7-cdf2014e8874\",\"name\":\"rerank-ft\",\"creator_id\":\"7a317d97-4d05-427d-9396-f31b9fb92c55\",\"organization_id\":\"6bdca3d5-3eae-4de0-ac34-786d8063b7ee\",\"settings\":{\"base_model\":{\"name\":\"english\",\"version\":\"2.0.0\",\"base_type\":\"BASE_TYPE_RERANK\",\"strategy\":\"STRATEGY_VANILLA\"},\"dataset_id\":\"rerank-dataset-d820xf\"},\"status\":\"STATUS_READY\",\"created_at\":\"2024-01-17T20:17:16Z\",\"updated_at\":\"2024-01-17T20:50:11Z\",\"completed_at\":\"2024-01-17T20:42:55Z\",\"last_used\":\"2024-01-15T09:30:00Z\"}],\"next_page_token\":\"next_page_token\",\"total_size\":1}"));
        ListFinetunedModelsResponse response = client.finetuning()
                .listFinetunedModels(FinetuningListFinetunedModelsRequest.builder()
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
                + "  \"finetuned_models\": [\n"
                + "    {\n"
                + "      \"id\": \"fee37446-7fc7-42f9-a026-c6ba2fcc422d\",\n"
                + "      \"name\": \"chat-ft\",\n"
                + "      \"creator_id\": \"7a317d97-4d05-427d-9396-f31b9fb92c55\",\n"
                + "      \"organization_id\": \"6bdca3d5-3eae-4de0-ac34-786d8063b7ee\",\n"
                + "      \"settings\": {\n"
                + "        \"base_model\": {\n"
                + "          \"name\": \"medium\",\n"
                + "          \"version\": \"14.2.0\",\n"
                + "          \"base_type\": \"BASE_TYPE_CHAT\",\n"
                + "          \"strategy\": \"STRATEGY_TFEW\"\n"
                + "        },\n"
                + "        \"dataset_id\": \"my-dataset-d701tr\",\n"
                + "        \"hyperparameters\": {\n"
                + "          \"early_stopping_patience\": 6,\n"
                + "          \"early_stopping_threshold\": 0.01,\n"
                + "          \"train_batch_size\": 16,\n"
                + "          \"train_epochs\": 1,\n"
                + "          \"learning_rate\": 0.01\n"
                + "        }\n"
                + "      },\n"
                + "      \"status\": \"STATUS_READY\",\n"
                + "      \"created_at\": \"2024-01-17T20:11:42Z\",\n"
                + "      \"updated_at\": \"2024-01-17T20:31:06Z\",\n"
                + "      \"completed_at\": \"2024-01-17T20:31:05Z\",\n"
                + "      \"last_used\": \"2024-01-15T09:30:00Z\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"id\": \"9d927c5e-7598-4772-98b7-cdf2014e8874\",\n"
                + "      \"name\": \"rerank-ft\",\n"
                + "      \"creator_id\": \"7a317d97-4d05-427d-9396-f31b9fb92c55\",\n"
                + "      \"organization_id\": \"6bdca3d5-3eae-4de0-ac34-786d8063b7ee\",\n"
                + "      \"settings\": {\n"
                + "        \"base_model\": {\n"
                + "          \"name\": \"english\",\n"
                + "          \"version\": \"2.0.0\",\n"
                + "          \"base_type\": \"BASE_TYPE_RERANK\",\n"
                + "          \"strategy\": \"STRATEGY_VANILLA\"\n"
                + "        },\n"
                + "        \"dataset_id\": \"rerank-dataset-d820xf\"\n"
                + "      },\n"
                + "      \"status\": \"STATUS_READY\",\n"
                + "      \"created_at\": \"2024-01-17T20:17:16Z\",\n"
                + "      \"updated_at\": \"2024-01-17T20:50:11Z\",\n"
                + "      \"completed_at\": \"2024-01-17T20:42:55Z\",\n"
                + "      \"last_used\": \"2024-01-15T09:30:00Z\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"next_page_token\": \"next_page_token\",\n"
                + "  \"total_size\": 1\n"
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
    public void testCreateFinetunedModel() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"finetuned_model\":{\"id\":\"id\",\"name\":\"name\",\"creator_id\":\"creator_id\",\"organization_id\":\"organization_id\",\"settings\":{\"base_model\":{\"base_type\":\"BASE_TYPE_UNSPECIFIED\"},\"dataset_id\":\"dataset_id\",\"multi_label\":true,\"wandb\":{\"project\":\"project\",\"api_key\":\"api_key\"}},\"status\":\"STATUS_UNSPECIFIED\",\"created_at\":\"2024-01-15T09:30:00Z\",\"updated_at\":\"2024-01-15T09:30:00Z\",\"completed_at\":\"2024-01-15T09:30:00Z\",\"last_used\":\"2024-01-15T09:30:00Z\"}}"));
        CreateFinetunedModelResponse response = client.finetuning()
                .createFinetunedModel(FinetunedModel.builder()
                        .name("name")
                        .settings(Settings.builder()
                                .baseModel(BaseModel.builder()
                                        .baseType(BaseType.BASE_TYPE_UNSPECIFIED)
                                        .build())
                                .datasetId("dataset_id")
                                .build())
                        .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"name\": \"name\",\n"
                + "  \"settings\": {\n"
                + "    \"base_model\": {\n"
                + "      \"base_type\": \"BASE_TYPE_UNSPECIFIED\"\n"
                + "    },\n"
                + "    \"dataset_id\": \"dataset_id\"\n"
                + "  }\n"
                + "}";
        JsonNode actualJson = objectMapper.readTree(actualRequestBody);
        JsonNode expectedJson = objectMapper.readTree(expectedRequestBody);
        Assertions.assertEquals(expectedJson, actualJson, "Request body structure does not match expected");
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
                + "  \"finetuned_model\": {\n"
                + "    \"id\": \"id\",\n"
                + "    \"name\": \"name\",\n"
                + "    \"creator_id\": \"creator_id\",\n"
                + "    \"organization_id\": \"organization_id\",\n"
                + "    \"settings\": {\n"
                + "      \"base_model\": {\n"
                + "        \"base_type\": \"BASE_TYPE_UNSPECIFIED\"\n"
                + "      },\n"
                + "      \"dataset_id\": \"dataset_id\",\n"
                + "      \"multi_label\": true,\n"
                + "      \"wandb\": {\n"
                + "        \"project\": \"project\",\n"
                + "        \"api_key\": \"api_key\"\n"
                + "      }\n"
                + "    },\n"
                + "    \"status\": \"STATUS_UNSPECIFIED\",\n"
                + "    \"created_at\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"updated_at\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"completed_at\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"last_used\": \"2024-01-15T09:30:00Z\"\n"
                + "  }\n"
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
    public void testGetFinetunedModel() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"finetuned_model\":{\"id\":\"fee37446-7fc7-42f9-a026-c6ba2fcc422d\",\"name\":\"chat-ft\",\"creator_id\":\"7a317d97-4d05-427d-9396-f31b9fb92c55\",\"organization_id\":\"6bdca3d5-3eae-4de0-ac34-786d8063b7ee\",\"settings\":{\"base_model\":{\"name\":\"medium\",\"version\":\"14.2.0\",\"base_type\":\"BASE_TYPE_CHAT\",\"strategy\":\"STRATEGY_TFEW\"},\"dataset_id\":\"my-dataset-d701tr\",\"hyperparameters\":{\"early_stopping_patience\":6,\"early_stopping_threshold\":0.01,\"train_batch_size\":16,\"train_epochs\":1,\"learning_rate\":0.01},\"multi_label\":true,\"wandb\":{\"project\":\"project\",\"api_key\":\"api_key\"}},\"status\":\"STATUS_READY\",\"created_at\":\"2024-01-17T20:11:42Z\",\"updated_at\":\"2024-01-17T20:31:06Z\",\"completed_at\":\"2024-01-17T20:31:05Z\",\"last_used\":\"2024-01-15T09:30:00Z\"}}"));
        GetFinetunedModelResponse response = client.finetuning().getFinetunedModel("id");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"finetuned_model\": {\n"
                + "    \"id\": \"fee37446-7fc7-42f9-a026-c6ba2fcc422d\",\n"
                + "    \"name\": \"chat-ft\",\n"
                + "    \"creator_id\": \"7a317d97-4d05-427d-9396-f31b9fb92c55\",\n"
                + "    \"organization_id\": \"6bdca3d5-3eae-4de0-ac34-786d8063b7ee\",\n"
                + "    \"settings\": {\n"
                + "      \"base_model\": {\n"
                + "        \"name\": \"medium\",\n"
                + "        \"version\": \"14.2.0\",\n"
                + "        \"base_type\": \"BASE_TYPE_CHAT\",\n"
                + "        \"strategy\": \"STRATEGY_TFEW\"\n"
                + "      },\n"
                + "      \"dataset_id\": \"my-dataset-d701tr\",\n"
                + "      \"hyperparameters\": {\n"
                + "        \"early_stopping_patience\": 6,\n"
                + "        \"early_stopping_threshold\": 0.01,\n"
                + "        \"train_batch_size\": 16,\n"
                + "        \"train_epochs\": 1,\n"
                + "        \"learning_rate\": 0.01\n"
                + "      },\n"
                + "      \"multi_label\": true,\n"
                + "      \"wandb\": {\n"
                + "        \"project\": \"project\",\n"
                + "        \"api_key\": \"api_key\"\n"
                + "      }\n"
                + "    },\n"
                + "    \"status\": \"STATUS_READY\",\n"
                + "    \"created_at\": \"2024-01-17T20:11:42Z\",\n"
                + "    \"updated_at\": \"2024-01-17T20:31:06Z\",\n"
                + "    \"completed_at\": \"2024-01-17T20:31:05Z\",\n"
                + "    \"last_used\": \"2024-01-15T09:30:00Z\"\n"
                + "  }\n"
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
    public void testDeleteFinetunedModel() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{\"key\":\"value\"}"));
        Map<String, Object> response = client.finetuning().deleteFinetunedModel("id");
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("DELETE", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = "" + "{\n" + "  \"key\": \"value\"\n" + "}";
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
    public void testUpdateFinetunedModel() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"finetuned_model\":{\"id\":\"id\",\"name\":\"name\",\"creator_id\":\"creator_id\",\"organization_id\":\"organization_id\",\"settings\":{\"base_model\":{\"base_type\":\"BASE_TYPE_UNSPECIFIED\"},\"dataset_id\":\"dataset_id\",\"multi_label\":true,\"wandb\":{\"project\":\"project\",\"api_key\":\"api_key\"}},\"status\":\"STATUS_UNSPECIFIED\",\"created_at\":\"2024-01-15T09:30:00Z\",\"updated_at\":\"2024-01-15T09:30:00Z\",\"completed_at\":\"2024-01-15T09:30:00Z\",\"last_used\":\"2024-01-15T09:30:00Z\"}}"));
        UpdateFinetunedModelResponse response = client.finetuning()
                .updateFinetunedModel(
                        "id",
                        FinetuningUpdateFinetunedModelRequest.builder()
                                .name("name")
                                .settings(Settings.builder()
                                        .baseModel(BaseModel.builder()
                                                .baseType(BaseType.BASE_TYPE_UNSPECIFIED)
                                                .build())
                                        .datasetId("dataset_id")
                                        .build())
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PATCH", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"name\": \"name\",\n"
                + "  \"settings\": {\n"
                + "    \"base_model\": {\n"
                + "      \"base_type\": \"BASE_TYPE_UNSPECIFIED\"\n"
                + "    },\n"
                + "    \"dataset_id\": \"dataset_id\"\n"
                + "  }\n"
                + "}";
        JsonNode actualJson = objectMapper.readTree(actualRequestBody);
        JsonNode expectedJson = objectMapper.readTree(expectedRequestBody);
        Assertions.assertEquals(expectedJson, actualJson, "Request body structure does not match expected");
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
                + "  \"finetuned_model\": {\n"
                + "    \"id\": \"id\",\n"
                + "    \"name\": \"name\",\n"
                + "    \"creator_id\": \"creator_id\",\n"
                + "    \"organization_id\": \"organization_id\",\n"
                + "    \"settings\": {\n"
                + "      \"base_model\": {\n"
                + "        \"base_type\": \"BASE_TYPE_UNSPECIFIED\"\n"
                + "      },\n"
                + "      \"dataset_id\": \"dataset_id\",\n"
                + "      \"multi_label\": true,\n"
                + "      \"wandb\": {\n"
                + "        \"project\": \"project\",\n"
                + "        \"api_key\": \"api_key\"\n"
                + "      }\n"
                + "    },\n"
                + "    \"status\": \"STATUS_UNSPECIFIED\",\n"
                + "    \"created_at\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"updated_at\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"completed_at\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"last_used\": \"2024-01-15T09:30:00Z\"\n"
                + "  }\n"
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
    public void testListEvents() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"events\":[{\"user_id\":\"7a317d97-4d05-427d-9396-f31b9fb92c55\",\"status\":\"STATUS_QUEUED\",\"created_at\":\"2024-01-17T20:11:45Z\"},{\"user_id\":\"7a317d97-4d05-427d-9396-f31b9fb92c55\",\"status\":\"STATUS_FINETUNING\",\"created_at\":\"2024-01-17T20:11:46Z\"},{\"user_id\":\"7a317d97-4d05-427d-9396-f31b9fb92c55\",\"status\":\"STATUS_DEPLOYING_API\",\"created_at\":\"2024-01-17T20:31:05Z\"},{\"user_id\":\"user_id\",\"status\":\"STATUS_READY\",\"created_at\":\"2024-01-17T20:31:06Z\"}],\"next_page_token\":\"next_page_token\",\"total_size\":5}"));
        ListEventsResponse response = client.finetuning()
                .listEvents(
                        "finetuned_model_id",
                        FinetuningListEventsRequest.builder()
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
                + "  \"events\": [\n"
                + "    {\n"
                + "      \"user_id\": \"7a317d97-4d05-427d-9396-f31b9fb92c55\",\n"
                + "      \"status\": \"STATUS_QUEUED\",\n"
                + "      \"created_at\": \"2024-01-17T20:11:45Z\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"user_id\": \"7a317d97-4d05-427d-9396-f31b9fb92c55\",\n"
                + "      \"status\": \"STATUS_FINETUNING\",\n"
                + "      \"created_at\": \"2024-01-17T20:11:46Z\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"user_id\": \"7a317d97-4d05-427d-9396-f31b9fb92c55\",\n"
                + "      \"status\": \"STATUS_DEPLOYING_API\",\n"
                + "      \"created_at\": \"2024-01-17T20:31:05Z\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"user_id\": \"user_id\",\n"
                + "      \"status\": \"STATUS_READY\",\n"
                + "      \"created_at\": \"2024-01-17T20:31:06Z\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"next_page_token\": \"next_page_token\",\n"
                + "  \"total_size\": 5\n"
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
    public void testListTrainingStepMetrics() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"step_metrics\":[{\"created_at\":\"2024-01-17T20:24:26Z\",\"step_number\":1,\"metrics\":{\"accuracy\":0.4557601809501648,\"cross_entropy\":4.264331340789795,\"generation_accuracy\":0.4557601809501648,\"generation_cross_entropy\":4.264331340789795,\"step\":0}},{\"created_at\":\"2024-01-17T20:25:19Z\",\"step_number\":9,\"metrics\":{\"accuracy\":0.7393720149993896,\"cross_entropy\":0.7702581286430359,\"generation_accuracy\":0.7393720149993896,\"generation_cross_entropy\":0.7702581286430359,\"step\":9}}],\"next_page_token\":\"next_page_token\"}"));
        ListTrainingStepMetricsResponse response = client.finetuning()
                .listTrainingStepMetrics(
                        "finetuned_model_id",
                        FinetuningListTrainingStepMetricsRequest.builder()
                                .pageSize(1)
                                .pageToken("page_token")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"step_metrics\": [\n"
                + "    {\n"
                + "      \"created_at\": \"2024-01-17T20:24:26Z\",\n"
                + "      \"step_number\": 1,\n"
                + "      \"metrics\": {\n"
                + "        \"accuracy\": 0.4557601809501648,\n"
                + "        \"cross_entropy\": 4.264331340789795,\n"
                + "        \"generation_accuracy\": 0.4557601809501648,\n"
                + "        \"generation_cross_entropy\": 4.264331340789795,\n"
                + "        \"step\": 0\n"
                + "      }\n"
                + "    },\n"
                + "    {\n"
                + "      \"created_at\": \"2024-01-17T20:25:19Z\",\n"
                + "      \"step_number\": 9,\n"
                + "      \"metrics\": {\n"
                + "        \"accuracy\": 0.7393720149993896,\n"
                + "        \"cross_entropy\": 0.7702581286430359,\n"
                + "        \"generation_accuracy\": 0.7393720149993896,\n"
                + "        \"generation_cross_entropy\": 0.7702581286430359,\n"
                + "        \"step\": 9\n"
                + "      }\n"
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
