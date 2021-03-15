package com.sl.umps.dao;

import com.sl.common.vo.SysResources;

import java.util.List;

public interface ResourceDao {
    List<SysResources> getRolePermission( String roleCode);
}
