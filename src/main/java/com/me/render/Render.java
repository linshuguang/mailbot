package com.me.render;

import com.me.context.Context;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Formatter;

/**
 * Created by kenya on 2017/11/24.
 */
public class Render {
    private static final Logger LOGGER = LoggerFactory.getLogger(Render.class);

    protected String makeRealPath(Context context, String path) {
        String realPath = path;

        File file = new File(path);
        if (!file.isAbsolute()) {
            realPath = context.getWorkingPath() + File.separator + realPath;
        }

        return realPath;
    }

    public String exec(String...cmd){

        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream stdout = process.getInputStream();
            BufferedReader brCleanUp = new BufferedReader(new InputStreamReader(stdout));

            StringBuffer sBuf = new StringBuffer();
            String line;
            while ((line = brCleanUp.readLine()) != null) {
                sBuf.append(line);
            }
            brCleanUp.close();
            return sBuf.toString();
        }catch(Exception e){
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return "";
    }

    protected File createTempFile() throws Exception{
        return createTempFile(".json");
    }
    protected File createTempFile(String suffix){
        try {
            File tempFile = File.createTempFile("pt.data", suffix);
            return tempFile;
        }catch (Exception e){
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
    public Object eval(Context context, Object model) {
        return model;
    }

}
