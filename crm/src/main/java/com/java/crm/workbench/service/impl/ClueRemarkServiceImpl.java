package com.java.crm.workbench.service.impl;

import com.java.crm.workbench.domain.ClueRemark;
import com.java.crm.workbench.mapper.ClueRemarkMapper;
import com.java.crm.workbench.service.ClueRemarkService;
import com.java.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("clueRemarkService")
public class ClueRemarkServiceImpl implements ClueRemarkService {
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Override
    public List<ClueRemark> queryClueRemarkForDetailByClueId(String clueId) {
        return clueRemarkMapper.selectClueRemarkForDetailByClueId(clueId);
    }
}
