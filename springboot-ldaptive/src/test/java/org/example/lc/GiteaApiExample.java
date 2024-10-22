package org.example.lc;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class GiteaApiExample {
    private static final String GITEA_URL = "http://172.31.161.37:3000";
    private static final String ADMIN_API_TOKEN = "4e2bac6a5b6ebc1751d706f1588c43e4a6c475c6";

    public static void main(String[] args) {
        try {
            String username = "san.zhang003";
            String email = "san.zhang003@example.com";
            String password = "123456";

            createUser(username, email, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createUser(String username, String email, String password) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(GITEA_URL + "/api/v1/admin/users");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Authorization", "token " + ADMIN_API_TOKEN);

            String jsonInputString = String.format(
                    "{\"username\": \"%s\", \"email\": \"%s\", \"password\": \"%s\", \"must_change_password\": true}",
                    username, email, password
            );

            request.setEntity(new StringEntity(jsonInputString));

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int responseCode = response.getStatusLine().getStatusCode();
                if (responseCode == 201) { // HTTP Created
                    System.out.println("User created successfully.");
                } else {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    System.out.println("Failed to create user. Response code: " + responseCode);
                    System.out.println("Response body: " + responseBody);
                }
            }
        }
    }
}
