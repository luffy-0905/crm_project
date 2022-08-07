package com.java.crm.workbench.service.impl;

import com.java.crm.workbench.domain.ClueActivityRelation;
import com.java.crm.workbench.mapper.ClueActivityRelationMapper;
import com.java.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("clueActivityRelationService")
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Override
    public int saveCreateClueActivityRelationByBatch(List<ClueActivityRelation> list) {
        return clueActivityRelationMapper.insertClueActivityRelationByBatch(list);
    }

    @Override
    public int deleteClueActivityRelationByClueIdAndActivityId(ClueActivityRelation car) {
        return clueActivityRelationMapper.deleteClueActivityRelationByClueIdAndActivityId(car);
    }
}
