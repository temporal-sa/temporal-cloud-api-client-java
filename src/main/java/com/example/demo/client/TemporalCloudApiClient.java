package com.example.demo.client;

import com.google.api.Logging;
import io.grpc.Channel;
import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannelBuilder;
import temporal.api.cloud.cloudservice.v1.CloudServiceGrpc;
import temporal.api.cloud.cloudservice.v1.CloudServiceGrpc.CloudServiceBlockingStub;
import temporal.api.cloud.cloudservice.v1.CloudServiceGrpc.CloudServiceFutureStub;
import temporal.api.cloud.cloudservice.v1.CloudServiceGrpc.CloudServiceStub;
import temporal.api.cloud.cloudservice.v1.RequestResponse.GetNamespacesRequest;
import temporal.api.cloud.cloudservice.v1.RequestResponse.GetNamespacesResponse;
import temporal.api.cloud.namespace.v1.Message.Namespace;
import java.util.List;
import java.util.logging.Logger;

public class TemporalCloudApiClient {
    private final Logger logger = Logger.getLogger("foo");
    private final CloudServiceBlockingStub blockingStub;

    public TemporalCloudApiClient(String host, int port) {
        Channel channel = ManagedChannelBuilder.forAddress(host, port).build();
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
}
