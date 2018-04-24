package com.me.executor;

import com.me.context.Context;
import com.me.resource.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Executor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Executor.class);

    private String rid;

    private boolean transpose=false;

    public void setTranspose(boolean transpose) {
        this.transpose = transpose;
    }

    public boolean isTranspose() {
        return transpose;
    }

    public Executor(){
        this.rid = null;
    }

    protected  <T> List<List<T>> transpose(List<List<T>> table) {
        List<List<T>> ret = new ArrayList<List<T>>();
        final int N = table.get(0).size();
        for (int i = 0; i < N; i++) {
            List<T> col = new ArrayList<T>();
            for (List<T> row : table) {
                col.add(row.get(i));
            }
            ret.add(col);
        }
        return ret;
    }


    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        if(rid ==null || StringUtils.isBlank(rid)){
            this.rid = null;
        }else {
            this.rid = rid;
        }
    }


    protected String makeRealPath(Context context, String path) {
        String realPath = path;

        File file = new File(path);
        if (!file.isAbsolute()) {
            realPath = context.getWorkingPath() + File.separator + realPath;
        }

        return realPath;
    }

    public Object eval(Context context){
        return new Object();
    }

    protected Resource fetchResource(Context context){
        /*
        ResourceManager resourceManager = context.getResourceManager();
        UserProfile userProfile = new UserProfile();
        userProfile.setId(context.getUid());
        return resourceManager.fetchResource(userProfile, rid);
        */
        return context.fetchResource(rid);
    }


    protected  byte[] readFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            byte[] data = Files.readAllBytes(path);
            return data;
        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }


}
