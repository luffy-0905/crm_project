package com.java.crm.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileOutputStream;

/**
 * 使用poi生成excel文件
 */
public class CreateExcelTest {
    public static void main(String[] args) throws Exception {
        //创建HSSFWorkbook对象，对应一个excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        //使用wb创建HSSFSheet对象，对应文件中的一页
        HSSFSheet sheet = wb.createSheet("学生列表");
        //使用sheet创建HSSFROw对象，对应sheet的一行
        HSSFRow row = sheet.createRow(0);//行号，从0开始
        //使用row创建HSSFCell，对应row的列
        HSSFCell cell = row.createCell(0);//列的编号，从0开始
        cell.setCellValue("学号");
        cell=row.createCell(1);
        cell.setCellValue("姓名");
        cell=row.createCell(2);
        cell.setCellValue("年龄");

        //生成HSSFCellStyle对象
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//设置居中对齐
        //使用sheet创建一个HSSFRow对象，对应sheet中的10行
        for (int i = 1; i <=10; i++) {
            row= sheet.createRow(i);

            cell = row.createCell(0);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("100"+i);
            cell=row.createCell(1);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("王桥"+i);
            cell=row.createCell(2);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("20");
        }

        //调用工具函数生成excel文件
        FileOutputStream os = new FileOutputStream("D:\\百度网盘安装\\百度网盘文件下载\\CRM项目（SSM框架版）\\CreateExcelTest\\studentList.xls");
        wb.write(os);

        //关闭资源
        os.close();
        wb.close();

        System.out.println("=============create OK===================");
    }
}
