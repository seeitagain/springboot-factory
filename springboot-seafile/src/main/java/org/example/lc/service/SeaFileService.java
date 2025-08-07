package org.example.lc.service;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.example.lc.pojo.FileReq;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SeaFileService {

    /**
     * 获取token
     * @return
     */
    public String generateToken(){
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"username\":\"chen.li009\",\"password\":\"!!Xeh602Xeh602\"}");
        Request request = new Request.Builder()
                .url("http://clouddisk.i.northking.net/api2/auth-token/")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch (IOException e){
            log.error("SeaFileService.generateToken error:{}", e.getMessage());
        }
        return "";
    }

    /**
     * 查询指定目录下的文件
     * @return
     */
    public String searchByCondition(FileReq req){
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"time_from\":"+req.getTimeFrom()+",\"time_to\":"+req.getTimeTo()+"}");
        Request request = new Request.Builder()
                .url("http://clouddisk.i.northking.net/api2/search/?q=质量")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("Authorization", "Token "+req.getToken())
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch (IOException e){
            log.error("SeaFileService.generateToken error:{}", e.getMessage());
        }
        return "";
    }


    /**
     * 查询指定目录下的文件
     * @return
     */
    public String search(FileReq req){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://clouddisk.i.northking.net/api/v2.1/repos/f6db63a3-9048-45f9-bbfc-d612be259e48/dir/?p=/公开资料文档库")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Token "+req.getToken())
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch (IOException e){
            log.error("SeaFileService.generateToken error:{}", e.getMessage());
        }
        return "";
    }


    /**
     * 生成分享连接
     * @return
     */
    public String shareLink(){
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"repo_id\":\"f6db63a3-9048-45f9-bbfc-d612be259e48\",\"path\":\"/公开资料文档库/2024年中国大数据行业应用探析-大数据驱动未来 深度解析大数据行业应用趋势与前景-头豹研究院.pdf\"}");
        Request request = new Request.Builder()
                .url("http://clouddisk.i.northking.net/api/v2.1/share-links/")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Token e2eee138288b7efc4d449a563a607e026b8f9b16")
                .build();

        try {
            Response response = client.newCall(request).execute();
            //成功生成链接，则返回{"username":"chen.li009@northking.net","repo_id":"f6db63a3-9048-45f9-bbfc-d612be259e48","repo_name":"\u6280\u672f\u7814\u53d1\u90e8-\u534f\u4f5c\u8d44\u6599\u5e93","path":"/\u516c\u5f00\u8d44\u6599\u6587\u6863\u5e93/2024\u5e74\u4e2d\u56fd\u5927\u6570\u636e\u884c\u4e1a\u5e94\u7528\u63a2\u6790-\u5927\u6570\u636e\u9a71\u52a8\u672a\u6765 \u6df1\u5ea6\u89e3\u6790\u5927\u6570\u636e\u884c\u4e1a\u5e94\u7528\u8d8b\u52bf\u4e0e\u524d\u666f-\u5934\u8c79\u7814\u7a76\u9662.pdf","obj_name":"2024\u5e74\u4e2d\u56fd\u5927\u6570\u636e\u884c\u4e1a\u5e94\u7528\u63a2\u6790-\u5927\u6570\u636e\u9a71\u52a8\u672a\u6765 \u6df1\u5ea6\u89e3\u6790\u5927\u6570\u636e\u884c\u4e1a\u5e94\u7528\u8d8b\u52bf\u4e0e\u524d\u666f-\u5934\u8c79\u7814\u7a76\u9662.pdf","is_dir":false,"token":"71d65103c13b4ab0b6a5","link":"http://clouddisk.i.northking.net/f/71d65103c13b4ab0b6a5/","view_cnt":0,"ctime":"2024-12-04T10:39:48+08:00","expire_date":"","is_expired":false,"permissions":{"can_edit":false,"can_download":true,"can_upload":false},"password":"","can_edit":false}
            //其中link为我们需要的链接，在此链接后面拼接  ?dl=1   即为可直接下载文件的链接
            //如果链接之前已经生成过，会返回{"error_msg":"Share link 71d65103c13b4ab0b6a5 already exists."}
            //其中71d65103c13b4ab0b6a5 为链接token,人工拼接为 http://clouddisk.i.northking.net/f/71d65103c13b4ab0b6a5/?dl=1 即为下载链接
            return response.body().string();
        }catch (IOException e){
            log.error("SeaFileService.generateToken error:{}", e.getMessage());
        }
        return "";
    }
}
