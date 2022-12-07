package com.ljf.risk.engine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.dao.RoasterListDao;
import com.ljf.risk.engine.entity.RoasterList;
import com.ljf.risk.engine.service.RoasterListService;
import org.springframework.stereotype.Repository;

@Repository
public class RoasterListServiceImpl extends ServiceImpl<RoasterListDao, RoasterList> implements RoasterListService {
}
