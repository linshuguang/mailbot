package com.me.render;

import com.me.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kenya on 2017/11/28.
 */
public class BlackHole extends Render {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlackHole.class);

    @Override
    public Object eval(Context context, Object model) {
        LOGGER.debug("blackhole will endeavour everything");
        return null;
    }
}
