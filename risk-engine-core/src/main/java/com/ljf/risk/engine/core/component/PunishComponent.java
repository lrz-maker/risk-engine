package com.ljf.risk.engine.core.component;

import com.ljf.risk.engine.core.SpringContextComponent;
import com.ljf.risk.engine.core.constants.CurrentContext;
import com.ljf.risk.engine.core.component.load.AbstractComponent;
import com.ljf.risk.engine.core.punish.CustomPunish;
import com.ljf.risk.engine.entity.Punish;
import com.ljf.risk.engine.service.PunishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lijinfeng
 */
@Slf4j
@Component
public class PunishComponent extends AbstractComponent<Long, List<Punish>> {

    private final SpringContextComponent springContextComponent;

    private final PunishService punishService;

    private final ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor;

    public PunishComponent(SpringContextComponent springContextComponent, PunishService punishService, ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor) {
        this.springContextComponent = springContextComponent;
        this.punishService = punishService;
        this.asyncThreadPoolTaskExecutor = asyncThreadPoolTaskExecutor;
    }

    @Override
    public String getComponentName() {
        return "惩罚策略";
    }

    @Override
    public void load() {
        List<Punish> punishes = punishService.list();
        this.cache = punishes.stream().collect(Collectors.groupingByConcurrent(Punish::getRuleId));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public void punish(Long ruleId) {
        List<Punish> punishes = this.cache.get(ruleId);
        if (CollectionUtils.isNotEmpty(punishes)) {
            CurrentContext.Context context = CurrentContext.currentCtx();
            asyncThreadPoolTaskExecutor.submit(() -> {
                for (Punish punish : punishes) {
                    CustomPunish customPunish = springContextComponent.getBean(punish.getPunishCode().name(), CustomPunish.class);
                    customPunish.punish(context, punish);
                }
            });
        }
    }
}
