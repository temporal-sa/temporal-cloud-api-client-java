package com.example.demo.client;

import io.grpc.*;
import java.util.logging.Logger;

public class LoggingClientInterceptor implements ClientInterceptor {

    private static final Logger logger = Logger.getLogger(LoggingClientInterceptor.class.getName());

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {
        // Log the call details here if required
        // Example: logger.info("Intercepted call to " + method.getFullMethodName());

        return next.newCall(method, callOptions);
    }
}
