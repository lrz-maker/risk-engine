package com.ljf.risk.engine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.entity.Event;
import com.ljf.risk.engine.dao.EventDao;
import com.ljf.risk.engine.service.EventService;
import org.springframework.stereotype.Service;

/**
 * @author lijinfeng
 */
@Service
public class EventServiceImpl extends ServiceImpl<EventDao, Event> implements EventService {}
