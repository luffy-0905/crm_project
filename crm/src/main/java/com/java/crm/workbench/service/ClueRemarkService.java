package com.java.crm.workbench.service;

import com.java.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    List<ClueRemark> queryClueRemarkForDetailByClueId(String ClueId);
}
