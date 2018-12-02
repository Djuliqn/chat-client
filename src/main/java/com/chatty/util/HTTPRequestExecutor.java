package com.chatty.util;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HTTPRequestExecutor {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    private final String baseUrl;

    public HTTPRequestExecutor(String hostName, String port) {
        this.baseUrl = "http://" + hostName + ":" + port;
    }

    public CloseableHttpResponse executePost(String path, Map<String, String> params){
        try {
            HttpPost httppost = new HttpPost( baseUrl + path);

            List<BasicNameValuePair> paramPairs = generateBasicNameValueParis(params);
            
            httppost.setEntity(new UrlEncodedFormEntity(paramPairs, "UTF-8"));

            //Execute and get the response.
            CloseableHttpResponse response = httpClient.execute(httppost);
            return response;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<BasicNameValuePair> generateBasicNameValueParis(Map<String, String> params) {
        List<BasicNameValuePair> paramPairs = new ArrayList<>(params.size());
        params.entrySet().forEach(p -> paramPairs.add(new BasicNameValuePair(p.getKey(), p.getValue())));
        return paramPairs;
    }

    public CloseableHttpResponse executeJSONPost(String path, Map<String, String> params){
        try {
            HttpPost httpPost = new HttpPost( baseUrl + path);

            StringEntity entity = generateJson(params);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = httpClient.execute(httpPost);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private StringEntity generateJson(Map<String, String> params) {
        StringBuilder strBuilder = new StringBuilder();
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        params.entrySet().forEach(entry -> jsonBuilder.add(entry.getKey(), entry.getValue()));
        try {
            return new StringEntity(jsonBuilder.build().toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        httpClient.close();
    }


}
