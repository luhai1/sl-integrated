package com.sl.umps.service;

import com.sl.common.vo.SysResources;

import java.util.List;

public interface ResourceService {
    List<SysResources> getRolePermission(String roleCode);

}
