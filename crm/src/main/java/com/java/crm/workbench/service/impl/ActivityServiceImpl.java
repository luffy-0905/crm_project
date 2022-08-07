package com.java.crm.workbench.service.impl;

import com.java.crm.workbench.domain.Activity;
import com.java.crm.workbench.mapper.ActivityMapper;
import com.java.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    public ActivityMapper activityMapper;
    @Override
    public int saveCreateActivity(Activity activity) {
        return activityMapper.insertActivity(activity);
    }

    @Override
    public List<Activity> queryActivityByConditionForPage(Map<String, Object> map) {
         return activityMapper.selectActivityByConditionForPage(map);
    }

    @Override
    public int queryCountOfActivityByCondition(Map<String, Object> map) {
        return activityMapper.selectCountOfActivityByCondition(map);
    }

    @Override
    public int deleteActivityById(String[] ids) {
        return activityMapper.deleteActivityById(ids);
    }

    @Override
    public Activity queryActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }

    @Override
    public int updateActivity(Activity activity) {
        return activityMapper.saveEditActivity(activity);
    }

    @Override
    public List<Activity> queryAllActivities() {
        return activityMapper.selectAllActivities();
    }

    @Override
    public List<Activity> queryPartActivityById(String[] id) {
        return activityMapper.selectPartActivityById(id);
    }

    @Override
    public int saveCreateActivityByBatch(List<Activity> activityList) {
        return activityMapper.insertActivityByBatch(activityList);
    }

    @Override
    public Activity queryActivityForDetailById(String id) {
        return activityMapper.selectActivityForDetailById(id);
    }

    @Override
    public List<Activity> queryActivityForDetailByClueId(String clueId) {
        return activityMapper.selectActivityForDetailByClueId(clueId);
    }

    @Override
    public List<Activity> queryActivityForDetailByNameAndClueId(Map<String, Object> map) {
        return activityMapper.selectActivityForDetailByNameAndClueId(map);
    }

    @Override
    public List<Activity> queryActivityForDetailByIds(String[] ids) {
        return activityMapper.selectActivityForDetailByIds(ids);
    }

    @Override
    public List<Activity> queryActivityForConvertByNameAndClueId(Map<String, Object> map) {
        return activityMapper.selectActivityForConvertByNameAndClueId(map);
    }

}
