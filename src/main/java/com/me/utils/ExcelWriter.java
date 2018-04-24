package com.me.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kenya on 2017/12/18.
 */
public class ExcelWriter {



    public static void forge(String fileName,String sheepStr, Object model ) {



        try {
            List<List<List<Object>>> dataSet = (List<List<List<Object>>>) model;
            XSSFWorkbook workbook = new XSSFWorkbook();
            int sheetNum = 0;
            for (List<List<Object>> data : dataSet) {

                sheetNum++;
                XSSFSheet sheet = workbook.createSheet(sheepStr + sheetNum);


                int rowNum = 0;

                for (List<Object> fields : data){

                    Row row = sheet.createRow(rowNum++);


                    int colNum = 0;
                    for (Object field : fields) {
                        Cell cell = row.createCell(colNum++);
                        if (field instanceof String) {
                            cell.setCellValue((String) field);
                        } else if (field instanceof Integer) {
                            cell.setCellValue((Integer) field);
                        } else if (field instanceof Float) {
                            cell.setCellValue((Float) field);
                        } else if (field instanceof Double) {
                            cell.setCellValue((Double) field);
                        } else if (field instanceof Date) {
                            cell.setCellValue((Date) field);
                        } else if (field instanceof Calendar) {
                            cell.setCellValue((Calendar) field);
                        } else if (field instanceof RichTextString) {
                            cell.setCellValue((RichTextString) field);
                        } else if (field instanceof Boolean) {
                            cell.setCellValue((Boolean) field);
                        } else if (field instanceof Byte) {
                            cell.setCellValue((Byte) field);
                        }
                    }
                }
            }
            FileOutputStream outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
