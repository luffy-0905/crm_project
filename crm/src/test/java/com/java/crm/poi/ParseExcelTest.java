package com.java.crm.poi;

import com.java.crm.commens.utils.HSSFUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;

/**
 * 使用apache-poi解析excel文件
 */
public class ParseExcelTest {
    public static void main(String[] args) throws Exception {
        //根据excel文件生成HSSFWorkbook对象,封装excel文件的所有信息
        FileInputStream is = new FileInputStream("D:\\百度网盘安装\\百度网盘文件下载\\CRM项目（SSM框架版）\\CreateExcelTest\\activityList.xls");
        HSSFWorkbook wb = new HSSFWorkbook(is);
        //根据wb获取HSSFSheet对象，封装了一页的所有信息
        HSSFSheet sheet = wb.getSheetAt(0);
        //根据sheet获取HSSFRow对象，封装了一行的所有信息
        HSSFRow row = null;
        HSSFCell cell = null;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {//sheet.getLastRowNum()最后一行的下表标
            row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {//row.getLastCellNum()最后一行的下表+1
                //根据row获取HSSFCell对象，封装一列的所有信息
                cell = row.getCell(j);

                //获取列中的数据
                System.out.print(HSSFUtils.getCellValueForStr(cell)+" ");
            }
            System.out.println();
        }
    }
}
