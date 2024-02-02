package com.example.demo.controllers;

import com.example.demo.client.TemporalCloudApiClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class TestController {

    private final TemporalCloudApiClient client = new TemporalCloudApiClient("saas-api.tmprl.cloud", 443);

    @GetMapping("/")
    public String doit() {
        StringBuilder html = new StringBuilder("<h2>Users</h2>");
        var users = client.getUsers();
        html.append("<table><tr><th>Id</th><th>Email</th><th>First Name</th><th>Created</th></tr>");
        for (var user : users) {
            // timestamp to human readable
            String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new Date(user.getCreatedTime().getSeconds() * 1000 + user.getCreatedTime().getNanos() / 1000000));

            html.append("<tr><td>").append(user.getId()).append("</td><td>")
                    .append(user.getSpec().getEmail()).append("</td><td>")
                    .append(user.getSpec().getAccess().getAccountAccess().getRole()).append("</td><td>")
                    .append(dateString).append("</td></tr>");
        }
        html.append("</table>");

        html.append("<br><br>");
        html.append("<h2>Namespaces</h2>");
        var namespaces = client.getNamespaces();
        html.append("<table><tr><th>Namespace</th><th>ID</th><th>Created</th></tr>");
        for (var namespace : namespaces) {
            // timestamp to human readable
            String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new Date(namespace.getCreatedTime().getSeconds() * 1000 + namespace.getCreatedTime().getNanos() / 1000000));

            html.append("<tr><td>").append(namespace.getNamespace()).append("</td><td>")
                    .append(namespace.getActiveRegion()).append("</td><td>")
                    .append(dateString).append("</td></tr>");
        }

        return html.toString();
    }
}
