package org.example.lc;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class GiteaApiExample3 {
    private static final String GITEA_URL = "https://gitea.example.com"; // 替换为你的 Gitea 服务器地址
    private static final String ADMIN_API_TOKEN = "YOUR_ADMIN_API_TOKEN"; // 替换为你的管理员 API Token

    public static void main(String[] args) {
        try {
            String owner = "your_owner"; // 替换为仓库拥有者的用户名
            String repo = "test"; // 替换为仓库名称
            String collaborator = "san.zhang002"; // 替换为被删除协作者的用户名

            removeUserPermission(owner, repo, collaborator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeUserPermission(String owner, String repo, String collaborator) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(GITEA_URL + "/api/v1/repos/" + owner + "/" + repo + "/collaborators/" + collaborator);
            request.setHeader("Authorization", "token " + ADMIN_API_TOKEN);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int responseCode = response.getStatusLine().getStatusCode();
                if (responseCode == 204) { // HTTP No Content
                    System.out.println("Permission removed successfully.");
                } else {
                    System.out.println("Failed to remove permission. Response code: " + responseCode);
                }
            }
        }
    }
}
