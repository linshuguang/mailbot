package com.me.render;

import com.me.context.Context;
import com.me.enums.EnvEnums;
import com.me.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.*;
import java.util.*;

/**
 * Created by kenya on 2017/11/28.
 */
public class HtmlTable extends  Render {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlTable.class);

    private final String _html_head       = "<html><body>";
    private final String _format_html_foot= "<p style=\"font-family: verdana,arial,sans-serif;font-size:10px;font-weight:bold;\">%s</p>";
    private final String _html_tail       = "</body></html>";
    private final String _html_p_head     = "<p style=\"font-family: verdana,arial,sans-serif;font-size:12px;font-weight:bold;\">%s</p>";

    private final String _table_head      = "<table style=\"font-family: verdana,arial,sans-serif;font-size:11px;color:#333333;border-width: 1px;border-color: #666666;border-collapse: collapse;\" border=\"1\"><tr>";
    private final String _format_table_th = "<th style=\"border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #dedede;\" nowrap>%s</th>";

    private final String _format_table_td = "<td style=\"border-width: 1px;padding: 8px;text-align: right;border-style: solid;border-color: #666666;background-color: #ffffff;\" align=\"right\" nowrap>%s</td>";
    private final String _table_tail      = "</table>";
    private final String _content         = "";

    private String cols;

    private String caption="";

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    private String makeColumns(){

        if(!StringUtils.isBlank(cols)) {
            try {
                Set<Integer> colSet = new HashSet<>();
                Set<String> hashSet = new HashSet<>(Arrays.asList(cols.split(",")));
                for (String h : hashSet) {
                    colSet.add(Integer.parseInt(h));
                }
                StringBuffer result = new StringBuffer();

                //StringBuffer sbuf = new StringBuffer();
                Formatter fmt = new Formatter(result);
                for (Integer h : colSet) {
                    fmt.format("autoRowSpan(tb,1,%d);", h);
                    //result.append(sbuf);
                }
                return result.toString();
            } catch (Exception e) {
                LOGGER.error(ExceptionUtils.getStackTrace(e));
            }
        }
        return null;
    }

    private String runMagic(Context context, String data) throws Exception{

        File temphtmlFile = createTempFile(".html");
        FileWriter writer = new FileWriter(temphtmlFile);
        writer.write(data);
        writer.close();
        temphtmlFile.deleteOnExit();



        String reduceJs = FileUtils.readStringInClassPath(context.getReduceJs());
        StringBuffer sBuf = new StringBuffer();
        Formatter fmt = new Formatter(sBuf);
        //String replace = tempJsFile.getAbsolutePath()
        fmt.format(reduceJs, temphtmlFile.getAbsolutePath().replace("\\", "/"));

        File tempJsFile = createTempFile(".js");
        writer = new FileWriter(tempJsFile);
        writer.write(sBuf.toString());
        writer.close();
        tempJsFile.deleteOnExit();

        sBuf = new StringBuffer();
        fmt = new Formatter(sBuf);
        //String replace = tempJsFile.getAbsolutePath().replace("\\", "/");
        fmt.format("\"%s\" \"%s\"", context.getPhantomjs(),tempJsFile.getAbsolutePath());
        String result = exec(sBuf.toString());

        return result.trim();
    }

    private String mergeRowCell(String data, Context context){
        String result = null;
        try {
            String columns = makeColumns();
            if(columns != null) {
                String backbone = FileUtils.readStringInClassPath(context.getBackBoneHtml());
                StringBuffer sbuf = new StringBuffer();
                Formatter fmt = new Formatter(sbuf);
                fmt.format(backbone, columns, data);
                result = runMagic(context, sbuf.toString());
            }
        }catch(Exception e){
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }

        return result;

    }



    private String renderTable(List<List<Object>> data, Context context){
        StringBuffer result = new StringBuffer();


        Formatter fmt = new Formatter(result);


        fmt.format(_html_p_head, caption);
        fmt.format("%s", _table_head);
        fmt.format("%s", "<tr>");

        List<Object>th_info = data.get(0);
        for(Object th :th_info) {
            fmt.format(_format_table_th,th.toString());
        }
        fmt.format("%s", "</tr>");

        for(int i = 1;i < data.size(); i++) {
            List<Object> td_info= data.get(i);
            fmt.format("%s", "<tr>");
            for(Object td :td_info) {
                fmt.format(_format_table_td,td.toString());
            }
            fmt.format("%s", "</tr>");
        }
        result.append(_table_tail);


        String cookedData = mergeRowCell(result.toString(), context);
        if(cookedData != null) {
            StringBuffer sBuf = new StringBuffer();
            Formatter f = new Formatter(sBuf);
            f.format(_html_p_head, caption);
            f.format("%s%s%s", _table_head, cookedData, _table_tail);
            return sBuf.toString();
        }else{
            return result.toString();
        }
    }


    @Override
    public Object eval(Context context, Object model){

        List<List<List<Object>>> list = (List<List<List<Object>>>)model;

        List<Object> results = new ArrayList<>();
        for( List<List<Object>> l : list ){
            results.add(renderTable(l, context));
        }
        return results;
    }
}
