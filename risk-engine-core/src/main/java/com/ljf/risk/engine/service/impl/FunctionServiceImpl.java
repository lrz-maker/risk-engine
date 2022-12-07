package com.ljf.risk.engine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.entity.Function;
import com.ljf.risk.engine.dao.FunctionDao;
import com.ljf.risk.engine.service.FunctionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lijinfeng
 */
@Service
@Slf4j
public class FunctionServiceImpl extends ServiceImpl<FunctionDao, Function> implements FunctionService {
}
