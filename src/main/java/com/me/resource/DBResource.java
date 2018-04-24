package com.me.resource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.me.context.Context;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * Created by kenya on 2017/11/24.
 */
public class DBResource extends Resource{

    private static final Logger LOGGER = LoggerFactory.getLogger(DBResource.class);

    private String sql;

    private ComboPooledDataSource ds;

    public void setSql(String sql) {
        this.sql = sql;
    }

    public DBResource(String driver, String url, String user, String passwd){
        ds = new ComboPooledDataSource();
        ds.setJdbcUrl(url);
        ds.setUser(user);
        ds.setPassword(passwd);
        try {
            ds.setDriverClass(driver);
        }catch(Exception e){
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            ds = null;
        }

    }

    @Override
    public List<List> run(Context context){

        List<List> sheets = new ArrayList<>();

        Connection conn = null;

        try{
            conn = ds.getConnection();
            Statement stmt = conn.createStatement();
            stmt.setEscapeProcessing(true);

            //int index=0;
            boolean hasMoreResultSets = stmt.execute(sql);
            while(hasMoreResultSets || stmt.getUpdateCount() != -1) {
                List data = new ArrayList();
                if ( hasMoreResultSets ) {
                    //index++;
                    ResultSet rs = stmt.getResultSet();
                    data.add(this.makeColumn(rs));
                    while (rs.next()) {
                        ResultSetMetaData rsmd = rs.getMetaData();
                        ArrayList tmp = new ArrayList();
                        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                            //System.out.println(rsmd.getColumnTypeName(i));
                            //System.out.println(rsmd.getColumnType(i));
                            if (rsmd.getColumnType(i) == Types.DOUBLE) {
                                tmp.add(rs.getDouble(i));
                            } else if (rsmd.getColumnType(i) == Types.FLOAT) {
                                tmp.add(rs.getFloat(i));
                            } else if (rsmd.getColumnType(i) == Types.BIGINT) {
                                tmp.add(rs.getBigDecimal(i));
                            } else if (rsmd.getColumnType(i) == Types.INTEGER) {
                                tmp.add(rs.getInt(i));
                            } else if (rsmd.getColumnType(i) == Types.DECIMAL) {
                                tmp.add(rs.getBigDecimal(i));
                            } else if (rsmd.getColumnType(i) == Types.NUMERIC) {
                                tmp.add(rs.getDouble(i));
                            } else {
                                tmp.add(rs.getString(i));
                            }
                            //tmp.add(rs.getString(i));
                        }
                        data.add(tmp);
                    }
                    rs.close();
                    StringBuffer sbuf = new StringBuffer();
                    Formatter fmt = new Formatter(sbuf);
                    //fmt.format("sheet%d",index);
                    sheets.add(data);
                }
                hasMoreResultSets = stmt.getMoreResults();
            }
            stmt.close();
        } catch (SQLException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            LOGGER.error(sql);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }
        return sheets;
    }

    private List makeColumn(ResultSet rs) {

        ArrayList tmp = new ArrayList();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                tmp.add(rsmd.getColumnName(i));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tmp;
    }



}
