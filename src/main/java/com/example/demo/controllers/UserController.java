package com.example.demo.controllers;

import com.example.demo.client.TemporalCloudApiClient;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final TemporalCloudApiClient client = new TemporalCloudApiClient("saas-api.tmprl.cloud", 443);

    // Endpoint to serve the HTML form
    @GetMapping("/createUserForm")
    public String createUserForm() {
        return "<form action='/createUser' method='post'>" +
                "<label for='email'>Email:</label><br>" +
                "<input type='text' id='email' name='email'><br>" +
                "<label for='role'>Role:</label><br>" +
                "<select id='role' name='role'>" +
                "<option value='admin'>Admin</option>" +
                "<option value='developer'>Developer</option>" +
                "<option value='read'>Read</option>" +
                "</select><br><br>" +
                "<input type='submit' value='Create User'>" +
                "</form>";
    }

    // Endpoint to handle form submission
    @PostMapping("/createUser")
    public String createUser(@RequestParam String email, @RequestParam String role) {
        try {
            // Call the client method to create a user
            String userId = client.createUser(email, role);
            return "User " + userId + " invited successfully. <a href='/'>Home</a>";
        } catch (Exception e) {
            return "Error creating user: " + e.getMessage();
        }
    }
}
