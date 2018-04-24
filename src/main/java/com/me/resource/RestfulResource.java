package com.me.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.me.context.Context;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.IOUtils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * Created by kenya on 2017/12/12.
 */
public class RestfulResource extends Resource {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestfulResource.class);



    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public List<List> run(Context context){

        List<List> sheets = new ArrayList<>();

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        try{
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            // Read the contents of an entity and return it as a String.
            String jsonString = EntityUtils.toString(entity);
            ObjectMapper mapper = new ObjectMapper();
            sheets = mapper.readValue(jsonString, new TypeReference(){});

        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            LOGGER.error(url);
        }

        return sheets;
    }

}
