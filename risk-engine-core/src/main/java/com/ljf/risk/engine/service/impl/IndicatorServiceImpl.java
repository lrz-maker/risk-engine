package com.ljf.risk.engine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.entity.Indicator;
import com.ljf.risk.engine.dao.IndicatorDao;
import com.ljf.risk.engine.service.IndicatorService;
import org.springframework.stereotype.Service;

/**
 * @author lijinfeng
 */
@Service
public class IndicatorServiceImpl extends ServiceImpl<IndicatorDao, Indicator> implements IndicatorService {
}


