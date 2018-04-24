package com.me.executor;

import com.me.context.Context;
import com.me.resource.DBResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SQLExecutor extends Executor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SQLExecutor.class);

    private String sql;

    public SQLExecutor(String sql) {
        this.sql = sql;
    }

    public SQLExecutor() {
        this.sql = null;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }


    @Override
    public Object eval(Context context) {
        DBResource dbResource = (DBResource) fetchResource(context);

        if(dbResource!=null) {
            dbResource.setSql(sql);
            List<List> list = dbResource.run(context);

            List<List> result = new ArrayList<>();
            if(isTranspose()){
                for(List l: list){
                    result.add(transpose(l));
                }
            }else{
                result = list;
            }
            return result;
        }else{
            LOGGER.error("no resource available");
        }
        return null;
    }

}
