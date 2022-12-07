package com.ljf.risk.engine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.entity.ConditionGroup;
import com.ljf.risk.engine.dao.ConditionGroupDao;
import com.ljf.risk.engine.service.ConditionGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lijinfeng
 */
@Service
@Slf4j
public class ConditionGroupServiceImpl extends ServiceImpl<ConditionGroupDao, ConditionGroup> implements ConditionGroupService {
}
