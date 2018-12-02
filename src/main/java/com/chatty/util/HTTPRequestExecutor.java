package com.chatty.util;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HTTPRequestExecutor {

    private final String baseUrl;

    public HTTPRequestExecutor(String hostName, String port) {
        this.baseUrl = "http://" + hostName + ":" + port;
    }

    public CloseableHttpResponse executePost(String path, Map<String, String> params){
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
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

}
