package com.example.demo;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SSLTest {
    public static void main(String[] args) {
        String hostname = "saas-api.tmprl.cloud";
        int port = 443;

        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory factory = sc.getSocketFactory();

            // Open a connection and attempt SSL handshake
            try (SSLSocket socket = (SSLSocket) factory.createSocket(hostname, port)) {
                socket.startHandshake();
                System.out.println("Successfully connected to " + hostname + " on port " + port);
            }

            // Print the default trust store details
            printTrustStoreDetails();
        } catch (SSLHandshakeException e) {
            System.out.println("SSL Handshake failed: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("SSL connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printTrustStoreDetails() throws Exception {
        String trustStore = System.getProperty("javax.net.ssl.trustStore");
        if (trustStore == null) {
            System.out.println("No trust store specified (using default)");
        } else {
            System.out.println("Trust store path: " + trustStore);
        }

        String trustStorePassword = System.getProperty("javax.net.ssl.trustStorePassword", "");
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream fis = trustStore != null ? new FileInputStream(trustStore) : null) {
            ks.load(fis, trustStorePassword.toCharArray());
        }

        System.out.println("Trust store type: " + ks.getType());
        System.out.println("Trust store provider: " + ks.getProvider());
        System.out.println("Trust store size: " + ks.size());
    }
}
