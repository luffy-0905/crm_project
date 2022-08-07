package com.java.crm.workbench.web.controller;

import com.java.crm.commens.contants.Contants;
import com.java.crm.commens.domain.ReturnObject;
import com.java.crm.commens.utils.DateUtils;
import com.java.crm.commens.utils.UUIDUtils;
import com.java.crm.settings.domain.User;
import com.java.crm.workbench.domain.ActivityRemark;
import com.java.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ActivityRemarkController {
    @Autowired
    private ActivityRemarkService activityRemarkService;
    @RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
    @ResponseBody
    public Object saveCreateActivityRemark(ActivityRemark remark, HttpSession session){
        ReturnObject returnObject=new ReturnObject();
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //封装参数
        remark.setId(UUIDUtils.getUUID());
        remark.setCreateTime(DateUtils.formatDateTime(new Date()));
        remark.setCreateBy(user.getId());
        remark.setEditFlag(Contants.REMARK_EDIT_FLAG_NO_EDITED);
        //调用service方法，保存
        try {
            int ret = activityRemarkService.saveCreateActivityRemark(remark);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetDate(remark);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，清稍后再试");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，清稍后再试");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/activity/deleteActivityRemark.do")
    @ResponseBody
    public Object deleteActivityRemark(String id){
        ReturnObject returnObject=new ReturnObject();
        try {
            int ret = activityRemarkService.deleteActivityRemarkById(id);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，稍后再试");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，稍后再试");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/saveActivityRemark.do")
    @ResponseBody
    public Object saveActivityRemark(ActivityRemark remark,HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        ReturnObject returnObject=new ReturnObject();
        remark.setEditTime(DateUtils.formatDateTime(new Date()));
        remark.setEditBy(user.getId());
        remark.setEditFlag(Contants.REMARK_EDIT_FLAG_YES_EDITED);
        try {
            //封装参数
            int ret = activityRemarkService.saveEditActivityRemark(remark);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetDate(remark);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，稍后再试");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，稍后再试");
        }
        return returnObject;
    }
}
