package com.sl.umps.service.impl;


import com.sl.umps.dao.DictionaryDao;
import com.sl.umps.entity.DictionaryEntity;
import com.sl.umps.service.DictionaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Resource
    DictionaryDao dictionaryDao;


    @Override
    public List<DictionaryEntity> getAllDict() {
        return dictionaryDao.getAllDict();
    }


}
