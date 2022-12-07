package com.ljf.risk.engine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.dao.RoasterDao;
import com.ljf.risk.engine.entity.Roaster;
import com.ljf.risk.engine.service.RoasterService;
import org.springframework.stereotype.Repository;

@Repository
public class RoasterServiceImpl extends ServiceImpl<RoasterDao, Roaster> implements RoasterService {
}
