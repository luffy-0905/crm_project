package com.java.crm.workbench.service;

import com.java.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int saveCreateActivity(Activity activity);

    List<Activity> queryActivityByConditionForPage(Map<String,Object> map);

    int queryCountOfActivityByCondition(Map<String,Object> map);

    int deleteActivityById(String[] ids);

    Activity queryActivityById(String id);

    int updateActivity(Activity activity);

    List<Activity> queryAllActivities();

    List<Activity> queryPartActivityById(String[] id);

    int saveCreateActivityByBatch(List<Activity> activityList);

    Activity queryActivityForDetailById(String id);

    List<Activity> queryActivityForDetailByClueId(String clueId);

    List<Activity> queryActivityForDetailByNameAndClueId(Map<String,Object> map);

    List<Activity> queryActivityForDetailByIds(String[] ids);

    List<Activity> queryActivityForConvertByNameAndClueId(Map<String,Object> map);
}
