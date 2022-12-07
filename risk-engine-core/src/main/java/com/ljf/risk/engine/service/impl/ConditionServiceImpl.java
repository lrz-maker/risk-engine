package com.ljf.risk.engine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.entity.Condition;
import com.ljf.risk.engine.dao.ConditionDao;
import com.ljf.risk.engine.service.ConditionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lijinfeng
 */
@Service
@Slf4j
public class ConditionServiceImpl extends ServiceImpl<ConditionDao, Condition> implements ConditionService {

}
