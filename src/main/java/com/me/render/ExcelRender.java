package com.me.render;

import com.me.context.Context;
import com.me.utils.ExcelWriter;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;

/**
 * Created by kenya on 2017/12/18.
 */
public class ExcelRender extends Attachment {

    private String prefix = "attach";
    private String suffix = ".xlsx";

    private String sheet="sheet";

    private String name;

    public String getSheet() {
        return sheet;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object eval(Context context, Object model) {

        if(StringUtils.isBlank(name)){
            Random rand = new Random();
            int  n = rand.nextInt(50) + 1;
            name = prefix+n+suffix;
        }

        if(!name.endsWith(suffix)){
            name = name + suffix;
        }
        String path = makeRealPath(context,name);
        ExcelWriter.forge(path,sheet, model);

        return path;
    }
}
