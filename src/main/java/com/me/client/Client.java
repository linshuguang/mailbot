package com.me.client;

import java.io.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
/**
 * Created by kenya on 2018/1/2.
 */
public abstract class Client {

    public void upload(String url, String path) {
        CloseableHttpClient httpclient = null;
        try {
            //Enter your host and port number...
            HttpPost post = new HttpPost(url);
            post.addHeader("Content-Type","multipart/mixed; boundary=\"---Content Boundary\"");
            //path of local file and correct for rest of the files also

            String message = "This is a multipart post";
            // Create Multipart instance
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            FileInputStream zipFile=new FileInputStream(path);
            builder.addBinaryBody("zipFile", zipFile,
                    ContentType.create("application/zip"), path);
            builder.setBoundary("---Content Boundary");
            httpclient = HttpClientBuilder.create().build();
            HttpEntity entity = builder.build();
            post.setEntity(entity);
            // execute the post request
            HttpResponse response = httpclient.execute(post);
            // Read the response HTML
            if (response != null) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    // Read the response string if required
                    InputStream responseStream = responseEntity.getContent() ;
                    if (responseStream != null){
                        BufferedReader br = new BufferedReader (new InputStreamReader (responseStream)) ;
                        String responseLine = br.readLine() ;
                        String tempResponseString = "" ;
                        while (responseLine != null){
                            tempResponseString = tempResponseString + responseLine + System.getProperty("line.separator") ;
                            responseLine = br.readLine() ;
                        }
                        br.close() ;
                        if (tempResponseString.length() > 0){
                            System.out.println(tempResponseString);
                        }
                    }
                    responseStream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public void serve(String path, String entry, OutputStream outputStream){

    }
}
