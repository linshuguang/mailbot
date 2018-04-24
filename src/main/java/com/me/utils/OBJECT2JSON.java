package com.me.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by kenya on 2017/11/24.
 */
public class OBJECT2JSON {

    private static final Logger LOGGER = LoggerFactory.getLogger(OBJECT2JSON.class);


    public static String Transform(Object obj) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return "";
    }
    public static void main(String[] args) {

    }
}
