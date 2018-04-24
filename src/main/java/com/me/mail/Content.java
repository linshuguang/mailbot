package com.me.mail;

import com.me.context.Context;
import com.me.render.Attachment;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kenya on 2017/11/24.
 */
public class Content {

    private static final Logger LOGGER = LoggerFactory.getLogger(Content.class);

    //@JsonValue
    private List<Record> records;

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {

        this.records = records;
    }

    public Object eval(Context context) {

        Map<String,Object> result = new HashMap<>();
        List<Object> recordList = new ArrayList<>();
        List<Object> attachments = new ArrayList<>();
        int i = 0;
        for (Record record : records) {
            i++;
            LOGGER.info("record[{}] is running ", i);
            try{
                if(record.getRender() instanceof Attachment){
                    attachments.add(record.eval(context));
                }else {
                    recordList.add(record.eval(context));
                }
                LOGGER.info("record[{}] is completed ",i);
            }catch(Exception e){
                LOGGER.error("record[{}] failed ", i);
                LOGGER.error(ExceptionUtils.getStackTrace(e));
                break;
            }
        }
        result.put("records",recordList);
        result.put("attachments",attachments);
        return result;
    }
}
