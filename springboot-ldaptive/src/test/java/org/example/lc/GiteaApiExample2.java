package org.example.lc;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class GiteaApiExample2 {
    private static final String GITEA_URL = "http://172.31.161.37:3000";
    private static final String ADMIN_API_TOKEN = "4e2bac6a5b6ebc1751d706f1588c43e4a6c475c6";

    public static void main(String[] args) {
        try {
            String owner = "root"; // 替换为仓库拥有者的用户名
            String repo = "test"; // 替换为仓库名称
            String collaborator = "san.zhang002"; // 替换为被授权用户的用户名

            setUserPermission(owner, repo, collaborator, "write");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setUserPermission(String owner, String repo, String collaborator, String permission) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(GITEA_URL + "/api/v1/repos/" + owner + "/" + repo + "/collaborators/" + collaborator);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Authorization", "token " + ADMIN_API_TOKEN);

            String jsonInputString = String.format("{\"permission\": \"%s\"}", permission);

            request.setEntity(new StringEntity(jsonInputString));

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int responseCode = response.getStatusLine().getStatusCode();
                if (responseCode == 200) { // HTTP OK
                    System.out.println("Permission set successfully.");
                } else {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    System.out.println("Failed to set permission. Response code: " + responseCode);
                    System.out.println("Response body: " + responseBody);
                }
            }
        }
    }
}