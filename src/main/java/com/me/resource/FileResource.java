package com.me.resource;

import com.me.context.Context;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by kenya on 2017/11/30.
 */
public class FileResource extends Resource{

    private static final Logger LOGGER = LoggerFactory.getLogger(FileResource.class);

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private  byte[] readFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            byte[] data = Files.readAllBytes(path);
            return data;
        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    @Override
    public String run(Context context){

        String realPath = path;
        File file = new File(realPath);
        if (!file.isAbsolute()){
            realPath = context.getWorkingPath()+File.separator+ realPath;
        }
        String val = readFile(realPath).toString();
        return val;
    }



}
