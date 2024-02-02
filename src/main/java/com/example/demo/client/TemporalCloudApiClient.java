package com.example.demo.client;

import com.google.api.Logging;
import io.grpc.Channel;
import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannelBuilder;
import temporal.api.cloud.cloudservice.v1.CloudServiceGrpc;
import temporal.api.cloud.cloudservice.v1.CloudServiceGrpc.CloudServiceBlockingStub;
import temporal.api.cloud.cloudservice.v1.RequestResponse;
import temporal.api.cloud.cloudservice.v1.RequestResponse.GetNamespacesRequest;
import temporal.api.cloud.cloudservice.v1.RequestResponse.GetNamespacesResponse;
import temporal.api.cloud.identity.v1.Message;
import temporal.api.cloud.namespace.v1.Message.Namespace;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class TemporalCloudApiClient {
    private final Logger logger = Logger.getLogger("foo");

    private final String host;
    private final int port;
    private final CloudServiceBlockingStub blockingStub;

    public TemporalCloudApiClient(
            @Value("${temporal.cloud.api.host}") String host, // grab from application.properties
            @Value("${temporal.cloud.api.port}") int port) {

        this.host = host;
        this.port = port;

        logger.info("Connecting to Temporal Cloud API at " + host + ":" + port);

        Channel channel = ManagedChannelBuilder.forAddress(host, port)
                .useTransportSecurity()
                .build();

        String apiKey = System.getenv("TEMPORAL_CLOUD_API_KEY");
        if (apiKey == null) {
            apiKey = "";
        }

        HeaderClientInterceptor headerInterceptor = new HeaderClientInterceptor(apiKey);
        blockingStub = CloudServiceGrpc.newBlockingStub(ClientInterceptors.intercept(channel, headerInterceptor, new LoggingClientInterceptor()));
    }

    public List<Namespace> getNamespaces() {
        GetNamespacesRequest request = GetNamespacesRequest.newBuilder().build();
        try {
            GetNamespacesResponse namespacesResponse = blockingStub.getNamespaces(request);
            for (Namespace namespace : namespacesResponse.getNamespacesList()) {
                System.out.println(namespace);
            }
            return namespacesResponse.getNamespacesList();
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    public List<temporal.api.cloud.identity.v1.Message.User> getUsers() {
        temporal.api.cloud.cloudservice.v1.RequestResponse.GetUsersRequest request = temporal.api.cloud.cloudservice.v1.RequestResponse.GetUsersRequest.newBuilder().build();
        try {
            temporal.api.cloud.cloudservice.v1.RequestResponse.GetUsersResponse usersResponse = blockingStub.getUsers(request);
            for (temporal.api.cloud.identity.v1.Message.User user : usersResponse.getUsersList()) {
                System.out.println(user);
            }
            return usersResponse.getUsersList();
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    public String createUser(String email, String role) {
        Message.AccountAccess accountAccess = Message.AccountAccess.newBuilder()
                .setRole(role)
                .build();

        Message.Access access = Message.Access.newBuilder()
                .setAccountAccess(accountAccess)
                .build();

        Message.UserSpec userSpec = Message.UserSpec.newBuilder()
                .setEmail(email)
                .setAccess(access)
                .build();

        RequestResponse.CreateUserRequest request = RequestResponse.CreateUserRequest.newBuilder()
                .setSpec(userSpec)
                .build();

        try {
            // Make the gRPC call to create a user
            RequestResponse.CreateUserResponse response = blockingStub.createUser(request);
            return response.getUserId(); // Return the created user ID
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
            throw e;
        }
    }
}
