package com.sl.umps.service.impl;

import com.sl.common.vo.SysResources;
import com.sl.umps.dao.ResourceDao;
import com.sl.umps.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    ResourceDao resourceDao;
    @Override
    public List<SysResources> getRolePermission(String roleCode) {
        return resourceDao.getRolePermission(roleCode);
    }
}
