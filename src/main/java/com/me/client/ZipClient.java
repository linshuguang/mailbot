package com.me.client;

import com.me.service.ClientMain;
import com.me.utils.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLEncoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by kenya on 2018/1/2.
 */
public class ZipClient extends Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZipClient.class);

    private String prefix;
    private String suffix;
    private String url;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }





    @Override
    public void serve(String path, String entry, OutputStream outputStream){

        try {

            File source = new File(path);
            //File.separator + entry;
            if(source.isDirectory()) {
                //String fullPath = path + File.separator + entry;
                String fullPath = path + "/" + entry;
                File fullFile = new File(fullPath);
                if(fullFile.exists()) {

                    File zipFile = File.createTempFile(prefix, suffix);

                    FileUtils.zipFile(path, zipFile.getAbsolutePath());
                    FileBody fileBody = new FileBody(zipFile, ContentType.DEFAULT_BINARY);
                    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                    builder.addPart("file", fileBody);
                    HttpEntity entity = builder.build();

                    HttpPost request = new HttpPost(url + "?entry=" + URLEncoder.encode(entry));
                    request.setEntity(entity);
                    HttpClient client = HttpClientBuilder.create().build();
                    HttpResponse httpResponse = client.execute(request);
                    LOGGER.debug("response:{}",httpResponse);
                }
            }

        }catch(Exception e){
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
