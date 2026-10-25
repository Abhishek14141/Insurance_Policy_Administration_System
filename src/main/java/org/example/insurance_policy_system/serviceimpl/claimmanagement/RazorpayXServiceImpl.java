////package org.example.insurance_policy_system.serviceimpl.claimmanagement;
////
////import com.fasterxml.jackson.databind.JsonNode;
////import com.fasterxml.jackson.databind.ObjectMapper;
////import lombok.RequiredArgsConstructor;
////import org.example.insurance_policy_system.service.claimmanagement.RazorpayXService;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.stereotype.Service;
////
////import java.math.BigDecimal;
////import java.net.URI;
////import java.net.http.*;
////import java.nio.charset.StandardCharsets;
////import java.time.Duration;
////import java.util.*;
////
////@Service
////@RequiredArgsConstructor
////public class RazorpayXServiceImpl implements RazorpayXService {
////
////    private final ObjectMapper mapper;
////
////    private final HttpClient client = HttpClient.newBuilder()
////            .connectTimeout(Duration.ofSeconds(10))
////            .build();
////
////    @Value("${razorpay.x.base-url:https://api.razorpay.com}")
////    private String baseUrl;
////
////    @Value("${razorpay.x.key-id}")
////    private String keyId;
////
////    @Value("${razorpay.x.key-secret}")
////    private String keySecret;
////
////    @Value("${razorpay.x.account-number}")
////    private String sourceAccountNumber;
////
////    private JsonNode send(
////            String method,
////            String path,
////            Object body,
////            String idempotencyKey) {
////
////        try {
////            String credentials = keyId + ":" + keySecret;
////
////            String auth = Base64.getEncoder()
////                    .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
////
////            HttpRequest.Builder builder = HttpRequest.newBuilder(
////                            URI.create(baseUrl + path))
////                    .timeout(Duration.ofSeconds(30))
////                    .header("Authorization", "Basic " + auth)
////                    .header("Accept", "application/json");
////
////            if (idempotencyKey != null) {
////                builder.header("X-Payout-Idempotency", idempotencyKey);
////            }
////
////            if ("POST".equals(method)) {
////                builder.header("Content-Type", "application/json")
////                        .POST(HttpRequest.BodyPublishers.ofString(
////                                mapper.writeValueAsString(body)));
////            } else {
////                builder.GET();
////            }
////
////            HttpResponse<String> response = client.send(
////                    builder.build(),
////                    HttpResponse.BodyHandlers.ofString()
////            );
////
//////            if (response.statusCode() < 200 ||
//////                    response.statusCode() >= 300) {
//////
//////                throw new IllegalStateException(
//////                        "RazorpayX API returned HTTP " + response.statusCode()
//////                );
//////            }
////
////
////            if (response.statusCode() < 200 ||
////                    response.statusCode() >= 300) {
////
////                System.err.println("RazorpayX HTTP Status: "
////                        + response.statusCode());
////
////                // Use temporarily for debugging with test-only data.
////                // Mask account numbers and other sensitive values before sharing.
////                System.err.println("RazorpayX Error Response: "
////                        + response.body());
////
////                throw new IllegalStateException(
////                        "RazorpayX API returned HTTP " + response.statusCode()
////                );
////            }
////
////            return mapper.readTree(response.body());
////
////        } catch (InterruptedException e) {
////            Thread.currentThread().interrupt();
////            throw new IllegalStateException("RazorpayX interrupted", e);
////
////        } catch (java.io.IOException e) {
////            throw new IllegalStateException("RazorpayX request failed", e);
////        }
////    }
////
////    private String getRequiredId(JsonNode response) {
////
////        String id = response == null
////                ? ""
////                : response.path("id").asText("");
////
////        if (id.isBlank()) {
////            throw new IllegalStateException("RazorpayX did not return an ID");
////        }
////
////        return id;
////    }
////
////    @Override
////    public String createContact(String name, Long claimId) {
////
////        return getRequiredId(send(
////                "POST",
////                "/v1/contacts",
////                Map.of(
////                        "name", name,
////                        "type", "customer",
////                        "reference_id", "CLAIM-" + claimId
////                ),
////                null
////        ));
////    }
////
////    @Override
////    public String createFundAccount(
////            String contactId,
////            String name,
////            String accountNumber,
////            String ifsc) {
////
////        return getRequiredId(send(
////                "POST",
////                "/v1/fund_accounts",
////                Map.of(
////                        "contact_id", contactId,
////                        "account_type", "bank_account",
////                        "bank_account", Map.of(
////                                "name", name,
////                                "account_number", accountNumber,
////                                "ifsc", ifsc
////                        )
////                ),
////                null
////        ));
////    }
////
////    @Override
////    public JsonNode createPayout(
////            String fundAccountId,
////            BigDecimal amount,
////            String mode,
////            String idempotencyKey) {
////
////        if (amount == null || amount.signum() <= 0) {
////            throw new IllegalArgumentException("Invalid payout amount");
////        }
////
////        long amountInPaise = amount.movePointRight(2).longValueExact();
////
////        return send(
////                "POST",
////                "/v1/payouts",
////                Map.of(
////                        "account_number", sourceAccountNumber,
////                        "fund_account_id", fundAccountId,
////                        "amount", amountInPaise,
////                        "currency", "INR",
////                        "mode", mode,
////                        "purpose", "payout",
////                        "queue_if_low_balance", false
////                ),
////                idempotencyKey
////        );
////    }
////
////    @Override
////    public JsonNode getPayout(String payoutId) {
////
////        if (!payoutId.matches("[A-Za-z0-9_-]+")) {
////            throw new IllegalArgumentException("Invalid payout ID");
////        }
////
////        return send(
////                "GET",
////                "/v1/payouts/" + payoutId,
////                null,
////                null
////        );
////    }
////}
//
//
//
//
//package org.example.insurance_policy_system.serviceimpl.claimmanagement;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.example.insurance_policy_system.service.claimmanagement.RazorpayXService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.nio.charset.StandardCharsets;
//import java.time.Duration;
//import java.util.Base64;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class RazorpayXServiceImpl implements RazorpayXService {
//
//    private final ObjectMapper mapper;
//
//    private final HttpClient client = HttpClient.newBuilder()
//            .connectTimeout(Duration.ofSeconds(10))
//            .build();
//
//    @Value("${razorpay.x.base-url:https://api.razorpay.com}")
//    private String baseUrl;
//
//    @Value("${razorpay.x.key-id}")
//    private String keyId;
//
//    @Value("${razorpay.x.key-secret}")
//    private String keySecret;
//
//    @Value("${razorpay.x.account-number}")
//    private String sourceAccountNumber;
//
//    // ==========================================
//    // COMMON HTTP REQUEST METHOD
//    // ==========================================
//
//    private JsonNode send(
//            String method,
//            String path,
//            Object body,
//            String idempotencyKey) {
//
//        try {
//
//            String fullUrl = baseUrl.replaceAll("/+$", "") + path;
//
//            URI uri = URI.create(fullUrl);
//
//            // Prevent accidentally sending credentials
//            // to an unexpected host.
//            if (!"https".equalsIgnoreCase(uri.getScheme())
//                    || !"api.razorpay.com".equalsIgnoreCase(uri.getHost())
//                    || uri.getUserInfo() != null
//                    || uri.getPort() != -1) {
//
//                throw new IllegalStateException(
//                        "Invalid RazorpayX API base URL: " + fullUrl
//                );
//            }
//
//            // ======================================
//            // DEBUGGING (NO SECRET VALUES)
//            // ======================================
//
//            System.out.println();
//            System.out.println("========== RAZORPAYX DEBUG ==========");
//            System.out.println("HTTP Method: " + method);
//            System.out.println("Request URL: " + fullUrl);
//            System.out.println("=====================================");
//            System.out.println();
//
//            String credentials = keyId + ":" + keySecret;
//
//            String auth = Base64.getEncoder()
//                    .encodeToString(
//                            credentials.getBytes(StandardCharsets.UTF_8)
//                    );
//
//            HttpRequest.Builder builder = HttpRequest.newBuilder()
//                    .uri(uri)
//                    .timeout(Duration.ofSeconds(30))
//                    .header("Authorization", "Basic " + auth)
//                    .header("Accept", "application/json");
//
//            if (idempotencyKey != null
//                    && !idempotencyKey.isBlank()) {
//
//                builder.header(
//                        "X-Payout-Idempotency",
//                        idempotencyKey
//                );
//            }
//
//            if ("POST".equalsIgnoreCase(method)) {
//
//                builder.header("Content-Type", "application/json");
//
//                builder.POST(
//                        HttpRequest.BodyPublishers.ofString(
//                                mapper.writeValueAsString(body)
//                        )
//                );
//
//            } else if ("GET".equalsIgnoreCase(method)) {
//
//                builder.GET();
//
//            } else {
//
//                throw new IllegalArgumentException(
//                        "Unsupported HTTP method: " + method
//                );
//            }
//
//            // ======================================
//            // SEND REQUEST
//            // ======================================
//
//            HttpResponse<String> response = client.send(
//                    builder.build(),
//                    HttpResponse.BodyHandlers.ofString()
//            );
//
//            System.out.println(
//                    "RazorpayX HTTP Status: " + response.statusCode()
//            );
//
//            // ======================================
//            // HANDLE ERROR RESPONSE
//            // ======================================
//
//            if (response.statusCode() < 200
//                    || response.statusCode() >= 300) {
//
//                String errorDescription = "Unknown RazorpayX error";
//
//                try {
//
//                    JsonNode errorResponse =
//                            mapper.readTree(response.body());
//
//                    errorDescription = errorResponse
//                            .path("error")
//                            .path("description")
//                            .asText("Unknown RazorpayX error");
//
//                } catch (Exception ignored) {
//
//                    errorDescription =
//                            "Unable to parse RazorpayX error response";
//                }
//
//                System.err.println(
//                        "RazorpayX Error: " + errorDescription
//                );
//
//                throw new IllegalStateException(
//                        "RazorpayX API returned HTTP "
//                                + response.statusCode()
//                                + " - "
//                                + errorDescription
//                );
//            }
//
//            // ======================================
//            // SUCCESS RESPONSE
//            // ======================================
//
//            JsonNode jsonResponse = mapper.readTree(
//                    response.body()
//            );
//
//            System.out.println(
//                    "RazorpayX Request Successful"
//            );
//
//            return jsonResponse;
//
//        } catch (InterruptedException e) {
//
//            Thread.currentThread().interrupt();
//
//            throw new IllegalStateException(
//                    "RazorpayX request interrupted",
//                    e
//            );
//
//        } catch (IOException e) {
//
//            throw new IllegalStateException(
//                    "RazorpayX network request failed",
//                    e
//            );
//        }
//    }
//
//    // ==========================================
//    // EXTRACT REQUIRED ID
//    // ==========================================
//
//    private String getRequiredId(JsonNode response) {
//
//        String id = response == null
//                ? ""
//                : response.path("id").asText("");
//
//        if (id.isBlank()) {
//
//            throw new IllegalStateException(
//                    "RazorpayX did not return an ID"
//            );
//        }
//
//        return id;
//    }
//
//    // ==========================================
//    // CREATE CONTACT
//    // ==========================================
//
//    @Override
//    public String createContact(
//            String name,
//            Long claimId) {
//
//        System.out.println(
//                "Creating RazorpayX Contact for Claim ID: "
//                        + claimId
//        );
//
//        JsonNode response = send(
//                "POST",
//                "/v1/contacts",
//                Map.of(
//                        "name", name,
//                        "type", "customer",
//                        "reference_id", "CLAIM-" + claimId
//                ),
//                null
//        );
//
//        return getRequiredId(response);
//    }
//
//    // ==========================================
//    // CREATE FUND ACCOUNT
//    // ==========================================
//
//    @Override
//    public String createFundAccount(
//            String contactId,
//            String name,
//            String accountNumber,
//            String ifsc) {
//
//        System.out.println(
//                "Creating RazorpayX Fund Account"
//        );
//
//        JsonNode response = send(
//                "POST",
//                "/v1/fund_accounts",
//                Map.of(
//                        "contact_id", contactId,
//                        "account_type", "bank_account",
//                        "bank_account", Map.of(
//                                "name", name,
//                                "account_number", accountNumber,
//                                "ifsc", ifsc
//                        )
//                ),
//                null
//        );
//
//        return getRequiredId(response);
//    }
//
//    // ==========================================
//    // CREATE PAYOUT
//    // ==========================================
//
//    @Override
//    public JsonNode createPayout(
//            String fundAccountId,
//            BigDecimal amount,
//            String mode,
//            String idempotencyKey) {
//
//        if (amount == null || amount.signum() <= 0) {
//
//            throw new IllegalArgumentException(
//                    "Payout amount must be greater than zero"
//            );
//        }
//
//        if (fundAccountId == null
//                || fundAccountId.isBlank()) {
//
//            throw new IllegalArgumentException(
//                    "Fund Account ID is required"
//            );
//        }
//
//        if (idempotencyKey == null
//                || idempotencyKey.isBlank()) {
//
//            throw new IllegalArgumentException(
//                    "Payout idempotency key is required"
//            );
//        }
//
//        if (mode == null
//                || !(mode.equalsIgnoreCase("IMPS")
//                || mode.equalsIgnoreCase("NEFT")
//                || mode.equalsIgnoreCase("RTGS"))) {
//
//            throw new IllegalArgumentException(
//                    "Unsupported payout mode: " + mode
//            );
//        }
//
//        if (sourceAccountNumber == null
//                || sourceAccountNumber.isBlank()) {
//
//            throw new IllegalStateException(
//                    "RazorpayX source account number is missing"
//            );
//        }
//
//        long amountInPaise = amount
//                .movePointRight(2)
//                .longValueExact();
//
//        System.out.println();
//        System.out.println("========== PAYOUT DETAILS ==========");
//        System.out.println("Payment Mode: " + mode);
//        System.out.println("Amount in Paise: " + amountInPaise);
//        System.out.println("Fund Account ID Present: true");
//        System.out.println("Idempotency Key Present: true");
//        System.out.println("===================================");
//        System.out.println();
//
//        Map<String, Object> payoutRequest = Map.of(
//                "account_number", sourceAccountNumber,
//                "fund_account_id", fundAccountId,
//                "amount", amountInPaise,
//                "currency", "INR",
//                "mode", mode.toUpperCase(),
//                "purpose", "payout",
//                "queue_if_low_balance", false
//        );
//
//        return send(
//                "POST",
//                "/v1/payouts",
//                payoutRequest,
//                idempotencyKey
//        );
//    }
//
//    // ==========================================
//    // GET PAYOUT STATUS
//    // ==========================================
//
//    @Override
//    public JsonNode getPayout(String payoutId) {
//
//        if (payoutId == null
//                || !payoutId.matches("[A-Za-z0-9_-]+")) {
//
//            throw new IllegalArgumentException(
//                    "Invalid RazorpayX Payout ID"
//            );
//        }
//
//        System.out.println(
//                "Fetching RazorpayX Payout Status"
//        );
//
//        return send(
//                "GET",
//                "/v1/payouts/" + payoutId,
//                null,
//                null
//        );
//    }
//}


package org.example.insurance_policy_system.serviceimpl.claimmanagement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.insurance_policy_system.service.claimmanagement.RazorpayXService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RazorpayXServiceImpl implements RazorpayXService {

    private final ObjectMapper mapper;

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Value("${claim.payout.provider:mock}")
    private String provider;

    @Value("${razorpay.x.base-url:https://api.razorpay.com}")
    private String baseUrl;

    @Value("${razorpay.x.key-id:}")
    private String keyId;

    @Value("${razorpay.x.key-secret:}")
    private String keySecret;

    @Value("${razorpay.x.account-number:}")
    private String sourceAccountNumber;

    private final Map<String, JsonNode> mockPayouts =
            new ConcurrentHashMap<>();

    private final Map<String, String> mockIdempotency =
            new ConcurrentHashMap<>();

    private boolean isMock() {
        return "mock".equalsIgnoreCase(provider);
    }

    private JsonNode send(
            String method,
            String path,
            Object body,
            String idempotencyKey) {

        try {
            String url = baseUrl.replaceAll("/+$", "") + path;
            URI uri = URI.create(url);

            if (!"https".equalsIgnoreCase(uri.getScheme())
                    || !"api.razorpay.com".equalsIgnoreCase(uri.getHost())
                    || uri.getPort() != -1
                    || uri.getUserInfo() != null) {
                throw new IllegalStateException(
                        "Invalid RazorpayX API URL"
                );
            }

            String credentials = keyId + ":" + keySecret;

            String auth = Base64.getEncoder().encodeToString(
                    credentials.getBytes(StandardCharsets.UTF_8)
            );

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(Duration.ofSeconds(30))
                    .header("Authorization", "Basic " + auth)
                    .header("Accept", "application/json");

            if (idempotencyKey != null && !idempotencyKey.isBlank()) {
                builder.header("X-Payout-Idempotency", idempotencyKey);
            }

            if ("POST".equalsIgnoreCase(method)) {
                builder.header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(
                                mapper.writeValueAsString(body)
                        ));
            } else if ("GET".equalsIgnoreCase(method)) {
                builder.GET();
            } else {
                throw new IllegalArgumentException("Unsupported method");
            }

            System.out.println("RazorpayX Request: " + method + " " + url);

            HttpResponse<String> response = client.send(
                    builder.build(),
                    HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("RazorpayX HTTP: " + response.statusCode());

            if (response.statusCode() < 200
                    || response.statusCode() >= 300) {

                String description = "Provider request failed";

                try {
                    JsonNode error = mapper.readTree(response.body());
                    description = error.path("error")
                            .path("description")
                            .asText(description);
                } catch (Exception ignored) {
                }

                throw new IllegalStateException(
                        "RazorpayX HTTP " + response.statusCode()
                                + ": " + description
                );
            }

            return mapper.readTree(response.body());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Request interrupted", e);
        } catch (IOException e) {
            throw new IllegalStateException("Network error", e);
        }
    }

    private String requiredId(JsonNode response) {
        String id = response.path("id").asText("");

        if (id.isBlank()) {
            throw new IllegalStateException("Provider returned no ID");
        }

        return id;
    }

    @Override
    public String createContact(String name, Long claimId) {

        if (isMock()) {
            return "mock_cont_" + UUID.randomUUID()
                    .toString().replace("-", "");
        }

        return requiredId(send(
                "POST",
                "/v1/contacts",
                Map.of(
                        "name", name,
                        "type", "customer",
                        "reference_id", "CLAIM-" + claimId
                ),
                null
        ));
    }

    @Override
    public String createFundAccount(
            String contactId,
            String name,
            String accountNumber,
            String ifsc) {

        if (isMock()) {
            return "mock_fa_" + UUID.randomUUID()
                    .toString().replace("-", "");
        }

        return requiredId(send(
                "POST",
                "/v1/fund_accounts",
                Map.of(
                        "contact_id", contactId,
                        "account_type", "bank_account",
                        "bank_account", Map.of(
                                "name", name,
                                "account_number", accountNumber,
                                "ifsc", ifsc
                        )
                ),
                null
        ));
    }

    @Override
    public JsonNode createPayout(
            String fundAccountId,
            BigDecimal amount,
            String mode,
            String idempotencyKey) {

        if (fundAccountId == null || fundAccountId.isBlank()) {
            throw new IllegalArgumentException("Fund account required");
        }

        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new IllegalArgumentException("Idempotency key required");
        }

        if (mode == null
                || !(mode.equalsIgnoreCase("IMPS")
                || mode.equalsIgnoreCase("NEFT")
                || mode.equalsIgnoreCase("RTGS"))) {
            throw new IllegalArgumentException("Unsupported payout mode");
        }

        long amountInPaise = amount.movePointRight(2).longValueExact();

        if (isMock()) {

            String existingId = mockIdempotency.get(idempotencyKey);

            if (existingId != null) {
                return mockPayouts.get(existingId);
            }

            String payoutId = "mock_pout_"
                    + UUID.randomUUID().toString().replace("-", "");

            JsonNode payout = mapper.valueToTree(Map.of(
                    "id", payoutId,
                    "entity", "payout",
                    "status", "processed",
                    "amount", amountInPaise,
                    "currency", "INR",
                    "mode", mode.toUpperCase(),
                    "fund_account_id", fundAccountId,
                    "provider", "MOCK",
                    "utr", "MOCK-" + payoutId,
                    "message", "Simulated payout; no bank transfer"
            ));

            mockPayouts.put(payoutId, payout);
            mockIdempotency.put(idempotencyKey, payoutId);

            System.out.println(
                    "MOCK PAYOUT PROCESSED: " + payoutId
            );

            return payout;
        }

        if (sourceAccountNumber == null
                || sourceAccountNumber.isBlank()) {
            throw new IllegalStateException(
                    "RazorpayX source account is missing"
            );
        }

        return send(
                "POST",
                "/v1/payouts",
                Map.of(
                        "account_number", sourceAccountNumber,
                        "fund_account_id", fundAccountId,
                        "amount", amountInPaise,
                        "currency", "INR",
                        "mode", mode.toUpperCase(),
                        "purpose", "payout",
                        "queue_if_low_balance", false
                ),
                idempotencyKey
        );
    }

    @Override
    public JsonNode getPayout(String payoutId) {

        if (payoutId == null
                || !payoutId.matches("[A-Za-z0-9_-]+")) {
            throw new IllegalArgumentException("Invalid payout ID");
        }

        if (payoutId.startsWith("mock_pout_")) {

            if (!isMock()) {
                throw new IllegalStateException(
                        "Mock payout cannot be fetched in RazorpayX mode"
                );
            }

            JsonNode payout = mockPayouts.get(payoutId);

            if (payout == null) {
                throw new IllegalStateException(
                        "Mock payout not found in memory"
                );
            }

            return payout;
        }

        if (isMock()) {
            throw new IllegalStateException(
                    "Real payout cannot be fetched in mock mode"
            );
        }

        return send(
                "GET",
                "/v1/payouts/" + payoutId,
                null,
                null
        );
    }
}
