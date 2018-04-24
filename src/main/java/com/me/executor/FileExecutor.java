package com.me.executor;

import com.me.context.Context;
import com.me.resource.FileResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FileExecutor extends Executor {


    private static final Logger logger = LoggerFactory.getLogger(FileExecutor.class);

    private  String path;

    public FileExecutor( String path) {
        this.path = path;
    }



    @Override
    public Object eval(Context context) {

        FileResource fileResource = (FileResource) fetchResource(context);
        if(fileResource == null) {

            String realPath = makeRealPath(context, path);


            String val = readFile(realPath).toString();
            return val;
        }else{
            logger.error("no fileResource found");
        }

        return null;
    }
}
