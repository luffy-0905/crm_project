package com.java.crm.workbench.service;

import com.java.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {
    int saveCreateClueActivityRelationByBatch(List<ClueActivityRelation> list);

    int deleteClueActivityRelationByClueIdAndActivityId(ClueActivityRelation car);
}
