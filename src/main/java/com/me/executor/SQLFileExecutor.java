package com.me.executor;

import com.me.context.Context;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLFileExecutor extends SQLExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SQLFileExecutor.class);

    private String path;

    public SQLFileExecutor( String path ) {
        this.path = path;
    }



    @Override
    public Object eval(Context context) {

        try {
            String realPath = makeRealPath(context, path);
            String sql = readFile(realPath).toString();
            super.setSql(sql);
        }catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return super.eval(context);
    }

}
