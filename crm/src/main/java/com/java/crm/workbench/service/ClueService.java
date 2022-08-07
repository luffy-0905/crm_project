package com.java.crm.workbench.service;

import com.java.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    int saveCreateClue(Clue clue);
    List<Clue> queryClueByConditionForPage(Map<String,Object> map);
    int queryCountOfClueByCondition(Map<String,Object> map);
    Clue queryClueDetailById(String id);

    void saveConvert(Map<String,Object> map);
}
