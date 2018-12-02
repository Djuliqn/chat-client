package com.chatty.util;

import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HTTPResponseMessageExtractor {

    public static String getResponseErrorMessage(CloseableHttpResponse entity) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = entity.getEntity().getContent().read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            byte[] byteArray = buffer.toByteArray();

            String text = new String(byteArray, StandardCharsets.UTF_8);
            return  text;
        } catch (IOException e) {
            return "Error";
        }
    }
}
