package com.me.render;

import com.me.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kenya on 2017/11/28.
 */
public class Attachment extends Render {
    private static final Logger LOGGER = LoggerFactory.getLogger(Attachment.class);

    @Override
    public Object eval(Context context, Object model) {

        return model;
    }
}
