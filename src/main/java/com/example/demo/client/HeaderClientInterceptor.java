package com.example.demo.client;

import io.grpc.*;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener;

public class HeaderClientInterceptor implements ClientInterceptor {

    private final String apiKey;
    private static final Metadata.Key<String> AUTHORIZATION_HEADER_KEY = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);
    private static final Metadata.Key<String> CUSTOM_HEADER_KEY = Metadata.Key.of("temporal-cloud-api-version", Metadata.ASCII_STRING_MARSHALLER);

    public HeaderClientInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        return new SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                headers.put(AUTHORIZATION_HEADER_KEY, "Bearer " + apiKey);
                headers.put(CUSTOM_HEADER_KEY, "2023-10-01-00");
                super.start(new SimpleForwardingClientCallListener<RespT>(responseListener) {
                    @Override
                    public void onHeaders(Metadata headers) {
                        super.onHeaders(headers);
                    }
                }, headers);
            }
        };
    }
}
