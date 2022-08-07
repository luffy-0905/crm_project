package com.java.crm.workbench.web.controller;

import com.java.crm.commens.contants.Contants;
import com.java.crm.commens.domain.ReturnObject;
import com.java.crm.commens.utils.DateUtils;
import com.java.crm.commens.utils.UUIDUtils;
import com.java.crm.settings.domain.DicValue;
import com.java.crm.settings.domain.User;
import com.java.crm.settings.service.DicValueService;
import com.java.crm.settings.service.UserService;
import com.java.crm.workbench.domain.Activity;
import com.java.crm.workbench.domain.Clue;
import com.java.crm.workbench.domain.ClueActivityRelation;
import com.java.crm.workbench.domain.ClueRemark;
import com.java.crm.workbench.service.ActivityService;
import com.java.crm.workbench.service.ClueActivityRelationService;
import com.java.crm.workbench.service.ClueRemarkService;
import com.java.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;



@Controller
public class ClueController {
    @Autowired
    private UserService userService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private ClueService clueService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;

    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request){
        //调用service方法,查询动态数据
        List<User> userList = userService.queryAllUser();
        List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> clueStateList = dicValueService.queryDicValueByTypeCode("clueState");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        //保存到request中
        request.setAttribute("userList",userList);
        request.setAttribute("appellationList",appellationList);
        request.setAttribute("clueStateList",clueStateList);
        request.setAttribute("sourceList",sourceList);
        //请求转发
        return "workbench/clue/index";
    }

    @RequestMapping("/workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session){
        ReturnObject returnObject=new ReturnObject();
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //封装参数
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateTime(DateUtils.formatDateTime(new Date()));
        clue.setCreateBy(user.getId());
        try {
            //调用service层，保存创建的线索
            int ret = clueService.saveCreateClue(clue);
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
    @RequestMapping("/workbench/clue/queryClueByConditionForPage.do")
    @ResponseBody
    public Object queryClueByConditionForPage(String fullname,String company,String phone,String source,String owner,
                                                String mphone,String state,int pageNo, int pageSize){
        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("state",state);
        map.put("pageNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        //查询数据
        List<Clue> clueList = clueService.queryClueByConditionForPage(map);
        int totalRows = clueService.queryCountOfClueByCondition(map);
        //根据查询结果，响应信息
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("clueList",clueList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }
    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id,HttpServletRequest request){
        //调用service方法，查询数据
        Clue clue = clueService.queryClueDetailById(id);
        List<ClueRemark> clueRemarkList = clueRemarkService.queryClueRemarkForDetailByClueId(id);
        List<Activity> activityList = activityService.queryActivityForDetailByClueId(id);
        //把数据保存到request中
        request.setAttribute("clue",clue);
        request.setAttribute("clueRemarkList",clueRemarkList);
        request.setAttribute("activityList",activityList);
        return "workbench/clue/detail";
    }
    @RequestMapping("/workbench/clue/queryActivityForDetailByNameAndClueId.do")
    @ResponseBody
    public Object queryActivityForDetailByNameAndClueId(String activityName,String clueId){
        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        //调用service方法，查询市场活动
        List<Activity> activityList = activityService.queryActivityForDetailByNameAndClueId(map);
        return activityList;
    }
    @RequestMapping("/workbench/clue/saveClueActivityRelation.do")
    @ResponseBody
    public Object saveClueActivityRelation(String[] activityId,String clueId){
        ReturnObject returnObject=new ReturnObject();
        //封装参数
        ClueActivityRelation car=null;
        List<ClueActivityRelation> relationList=new ArrayList<>();
        for (String ai:activityId){
            car=new ClueActivityRelation();
            car.setId(UUIDUtils.getUUID());
            car.setActivityId(ai);
            car.setClueId(clueId);
            relationList.add(car);
        }
        //调用service方法，保存数据
        try {
            int ret = clueActivityRelationService.saveCreateClueActivityRelationByBatch(relationList);
            List<Activity> activityList = activityService.queryActivityForDetailByIds(activityId);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetDate(activityList);
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

    @RequestMapping("/workbench/clue/deleteClueActivityRelation.do")
    @ResponseBody
    public Object deleteClueActivityRelation(ClueActivityRelation car){
        ReturnObject returnObject=new ReturnObject();
        //调用service层方法，删除线索和市场活动的关联
        try {
            int ret = clueActivityRelationService.deleteClueActivityRelationByClueIdAndActivityId(car);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙稍后再试");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙稍后再试");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id,HttpServletRequest request){
        //调用service层,查询线索信息
        Clue clue = clueService.queryClueDetailById(id);
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("clue",clue);
        request.setAttribute("stageList",stageList);
        //请求转发
        return "workbench/clue/convert";
    }
    @RequestMapping("/workbench/clue/queryActivityForConvertByNameAndClueId.do")
    @ResponseBody
    public Object queryActivityForConvertByNameAndClueId(String activityName,String clueId){
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        //调用service层,查询市场活动
        List<Activity> activityList = activityService.queryActivityForConvertByNameAndClueId(map);
        return activityList;
    }
    @RequestMapping("/workbench/clue/saveConvertClue.do")
    @ResponseBody
    public Object saveConvertClue(String clueId,String money,String name,String expectedDate,String stage,String activityId,String isCreateTran,HttpSession session){
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("clueId",clueId);
        map.put("money",money);
        map.put("name",name);
        map.put("expectedDate",expectedDate);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("isCreateTran",isCreateTran);
        map.put(Contants.SESSION_USER,session.getAttribute(Contants.SESSION_USER));
        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service保存线索转换
            clueService.saveConvert(map);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，稍后再试");
        }
        return returnObject;
    }
}
