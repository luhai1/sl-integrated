package com.sl.umps.service.impl;


import com.sl.umps.dao.SysConfigDao;
import com.sl.umps.entity.SysConfig;
import com.sl.umps.service.SysConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysConfigServiceImpl implements SysConfigService {
    @Resource
    SysConfigDao sysConfigDao;
    @Override
    public List<SysConfig> getAllSysConfig() {
        return sysConfigDao.getAllSysConfig();
    }
}
