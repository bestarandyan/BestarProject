package com.huoqiu.framework.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;

public class Jackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
    private final static String TAG = "ManyiJSON";

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException {
        getObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            return super.readInternal(clazz, inputMessage);
    }

    public String getStringFromStream(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            baos.write(b, 0, n);
        }
        return baos.toString();
    }
}