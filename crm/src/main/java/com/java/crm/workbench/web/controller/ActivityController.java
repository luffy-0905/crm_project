package com.java.crm.workbench.web.controller;

import com.java.crm.commens.contants.Contants;
import com.java.crm.commens.domain.ReturnObject;
import com.java.crm.commens.utils.DateUtils;
import com.java.crm.commens.utils.HSSFUtils;
import com.java.crm.commens.utils.UUIDUtils;
import com.java.crm.settings.domain.User;
import com.java.crm.settings.service.UserService;
import com.java.crm.workbench.domain.Activity;
import com.java.crm.workbench.domain.ActivityRemark;
import com.java.crm.workbench.service.ActivityRemarkService;
import com.java.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class ActivityController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request) {
        List<User> users = userService.queryAllUser();
        request.setAttribute(Contants.REQUEST_USERS, users);
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //封装数据
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));
        activity.setCreateBy(user.getId());
        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service方法，保存创建的市场活动(根据ret返回结果判断是否操作成功)
            int ret = activityService.saveCreateActivity(activity);
            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙,稍后再试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙,稍后再试...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name, String owner, String startDate, String endDate,
                                                  int pageNo, int pageSize) {
        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("pageNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        //查询数据
        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        int totalRows = activityService.queryCountOfActivityByCondition(map);
        //根据查询结果，响应信息
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("activityList", activityList);
        retMap.put("totalRows", totalRows);
        return retMap;
    }

    @RequestMapping("/workbench/activity/deleteActivityById.do")
    @ResponseBody
    public Object deleteActivityById(String[] id) {
        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service，删除数据
            int ret = activityService.deleteActivityById(id);
            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙,请稍后再试.....");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setMessage("系统繁忙,请稍后再试.....");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id) {
        //查询
        Activity activity = activityService.queryActivityById(id);
        return activity;
    }

    @RequestMapping("/workbench/activity/updateActivity.do")
    @ResponseBody
    public Object updateActivity(Activity activity, HttpSession session) {
        ReturnObject returnObject = new ReturnObject();
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //封装参数
        try {
            activity.setEditBy(user.getId());
            activity.setEditTime(DateUtils.formatDateTime(new Date()));
            int ret = activityService.updateActivity(activity);
            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙,请稍后再试.....");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setMessage("系统繁忙,请稍后再试.....");
        }
        return returnObject;
    }

    //演示文件下载
    @RequestMapping("/workbench/activity/fileDownload.do")
    public void fileDownload(HttpServletResponse response) throws Exception {
        //设置响应类型
        response.setContentType("application/octet-stream;charset=utf-8");
        //获取输出流
        OutputStream os = response.getOutputStream();

        //设置响应头信息，是浏览器接受到响应信息之后，直接激活文件下载窗口，即使能打开也不打开
        response.addHeader("Content-Disposition", "attachment;filename=myStudentList.xls");

        //获取输入流(读取excel文件),输出到浏览器
        InputStream is = new FileInputStream("D:\\百度网盘安装\\百度网盘文件下载\\CRM项目（SSM框架版）\\CreateExcelTest\\studentList.xls");
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = is.read(bytes)) != -1) {
            os.write(bytes, 0, len);
        }
        is.close();
        //谁开启的资源谁关闭，这个是tomcat自己关闭，刷新以下就行
        os.flush();
    }

    @RequestMapping("/workbench/activity/exportAllActivities.do")
    public void exportAllActivities(HttpServletResponse response) throws Exception {
        //查询所有的活动
        List<Activity> activityList = activityService.queryAllActivities();
        //创建excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改者");
        Activity activity = null;
        for (int i = 0; i < activityList.size(); i++) {
            activity = activityList.get(i);
            //每遍历出一个activity，生成一行
            row = sheet.createRow(i + 1);
            cell = row.createCell(0);
            cell.setCellValue(activity.getId());
            cell = row.createCell(1);
            cell.setCellValue(activity.getOwner());
            cell = row.createCell(2);
            cell.setCellValue(activity.getName());
            cell = row.createCell(3);
            cell.setCellValue(activity.getStartDate());
            cell = row.createCell(4);
            cell.setCellValue(activity.getEndDate());
            cell = row.createCell(5);
            cell.setCellValue(activity.getCost());
            cell = row.createCell(6);
            cell.setCellValue(activity.getDescription());
            cell = row.createCell(7);
            cell.setCellValue(activity.getCreateTime());
            cell = row.createCell(8);
            cell.setCellValue(activity.getCreateBy());
            cell = row.createCell(9);
            cell.setCellValue(activity.getEditTime());
            cell = row.createCell(10);
            cell.setCellValue(activity.getEditBy());

        }
        //调用工具函数生成excel文件
            /*OutputStream os = new FileOutputStream("D:\\百度网盘安装\\百度网盘文件下载\\CRM项目（SSM框架版）\\CreateExcelTest\\activityList.xls");
            wb.write(os);
            //关闭资源
            os.close();
            wb.close();*/
        response.setContentType("application/octet-stream;charset=utf-8");
        response.addHeader("Content-Disposition", "attachment;filename=activityList.xls");
        OutputStream out = response.getOutputStream();
        //获取输入流(读取excel文件),输出到浏览器
            /*InputStream is = new FileInputStream("D:\\百度网盘安装\\百度网盘文件下载\\CRM项目（SSM框架版）\\CreateExcelTest\\activityList.xls");
            byte[] bytes=new byte[256];
            int len=0;
            while((len=is.read(bytes))!=-1){
                out.write(bytes,0,len);
            }
            is.close();*/
        //直接内存到内存写入
        wb.write(out);
        wb.close();
        out.flush();
    }

    @RequestMapping("/workbench/activity/exportPartActivityById.do")
    public void exportPartActivityById(String[] id, HttpServletResponse response) throws Exception {
        //查询活动
        List<Activity> activityList = activityService.queryPartActivityById(id);
        //创建excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改者");
        Activity activity = null;
        for (int i = 0; i < activityList.size(); i++) {
            activity = activityList.get(i);
            //每遍历出一个activity，生成一行
            row = sheet.createRow(i + 1);
            cell = row.createCell(0);
            cell.setCellValue(activity.getId());
            cell = row.createCell(1);
            cell.setCellValue(activity.getOwner());
            cell = row.createCell(2);
            cell.setCellValue(activity.getName());
            cell = row.createCell(3);
            cell.setCellValue(activity.getStartDate());
            cell = row.createCell(4);
            cell.setCellValue(activity.getEndDate());
            cell = row.createCell(5);
            cell.setCellValue(activity.getCost());
            cell = row.createCell(6);
            cell.setCellValue(activity.getDescription());
            cell = row.createCell(7);
            cell.setCellValue(activity.getCreateTime());
            cell = row.createCell(8);
            cell.setCellValue(activity.getCreateBy());
            cell = row.createCell(9);
            cell.setCellValue(activity.getEditTime());
            cell = row.createCell(10);
            cell.setCellValue(activity.getEditBy());
        }
        response.setContentType("application/octet-stream;charset=utf-8");
        response.addHeader("Content-Disposition", "attachment;filename=activityPartList.xls");
        OutputStream out = response.getOutputStream();
        wb.write(out);
        wb.close();
        out.flush();
    }

    //演示文件上传
    //配置springMVC的文件上传解析器
    @RequestMapping("/workbench/activity/fileUpLoad.do")
    @ResponseBody
    public Object fileUpLoad(String userName, MultipartFile myFile) throws Exception {
        //把文本数据打印到控制台
        System.out.println("userName = " + userName);
        //把文件在服务指定的目录中生成同一个文件
        String originalFilename = myFile.getOriginalFilename();
        File file = new File("D:\\百度网盘安装\\百度网盘文件下载\\CRM项目（SSM框架版）\\CreateExcelTest\\" + originalFilename);
        myFile.transferTo(file);

        //返回响应信息
        ReturnObject returnObject = new ReturnObject();
        returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        returnObject.setMessage("上传成功");
        return returnObject;
    }

    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile, HttpSession session) {
        //把excel文件写道磁盘中
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        ReturnObject returnObject = new ReturnObject();
        try {
            /*String originalFilename = activityFile.getOriginalFilename();
            File file = new File("D:\\百度网盘安装\\百度网盘文件下载\\CRM项目（SSM框架版）\\CreateExcelTest\\", originalFilename);
            activityFile.transferTo(file);
            InputStream is = new FileInputStream("D:\\百度网盘安装\\百度网盘文件下载\\CRM项目（SSM框架版）\\CreateExcelTest\\" + originalFilename);*/

            InputStream is = activityFile.getInputStream();
            HSSFWorkbook wb = new HSSFWorkbook(is);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row = null;
            HSSFCell cell = null;
            Activity activity = null;
            List<Activity> activityList = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);//从第二行开始，第一行是表头
                activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DateUtils.formatDateTime(new Date()));
                activity.setCreateBy(user.getId());
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    //获取列中的数据
                    String cellValue = HSSFUtils.getCellValueForStr(cell);
                    if (j == 0) {
                        activity.setName(cellValue);
                    } else if (j == 1) {
                        activity.setStartDate(cellValue);
                    } else if (j == 2) {
                        activity.setEndDate(cellValue);
                    } else if (j == 3) {
                        activity.setCost(cellValue);
                    } else if (j == 4) {
                        activity.setDescription(cellValue);
                    }
                }
                //每一行中的所有列都封装完了之后，把activity保存到list中
                activityList.add(activity);
            }
            //调用service方法，保存市场活动
            int ret = activityService.saveCreateActivityByBatch(activityList);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetDate(ret);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，稍后再试。");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/detailActivity.do")
    public String detailActivity(String id,HttpServletRequest request){
        //调用service方法，查询数据
        Activity activity = activityService.queryActivityForDetailById(id);
        List<ActivityRemark> remarkList = activityRemarkService.queryActivityRemarkForDetailByActivityId(id);
        //把数据保存到request中
        request.setAttribute("activity",activity);
        request.setAttribute("remarkList",remarkList);
        //请求转发
        return "workbench/activity/detail";
    }
}
