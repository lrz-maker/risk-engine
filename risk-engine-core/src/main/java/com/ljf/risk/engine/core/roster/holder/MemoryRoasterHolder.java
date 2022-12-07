package com.ljf.risk.engine.core.roster.holder;

import com.google.common.base.Objects;
import com.ljf.risk.engine.core.roster.RoasterHolder;
import com.ljf.risk.engine.core.component.load.ComponentLoad;
import com.ljf.risk.engine.entity.Roaster;
import com.ljf.risk.engine.entity.RoasterList;
import com.ljf.risk.engine.service.RoasterListService;
import com.ljf.risk.engine.service.RoasterService;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 基于内存的名单holder
 * 可靠，但是耗内存
 *
 * @author weiweihan
 */
public class MemoryRoasterHolder extends AbstractRoasterHolder<DelayQueue<MemoryRoasterHolder.Item>, MemoryRoasterHolder.Item, DelayQueue<MemoryRoasterHolder.Item>> implements RoasterHolder, ComponentLoad, Runnable {
    private ThreadLocal<Map<Item, DelayQueue<Item>>> mapThreadLocal = ThreadLocal.withInitial(HashMap::new);

    public MemoryRoasterHolder(RoasterService roasterService, RoasterListService roasterListService) {
        super(roasterService, roasterListService);
    }

    @Override
    public Set<String> getRoaster(String code) {

        DelayQueue<Item> items = cache.get(new Item(code, null));
        if (items == null) {
            return new HashSet<>();
        }
        Set<String> set = new HashSet<>();
        items.forEach(item -> set.add(item.target));
        return set;
    }

    @Override
    public void refresh() {
        this.load();
    }

    @Override
    public boolean contains(String code, String value) {
        return getRoaster(code).contains(value);
    }

    @Override
    protected DelayQueue<Item> prepare(Roaster roaster) {
        return new DelayQueue<>();
    }

    @Override
    protected void onList(Roaster roaster, RoasterList roasterList, DelayQueue<Item> items) {
        items.add(new Item(roasterList.getValue(), roasterList.getExpiredAt()));
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        cache = mapThreadLocal.get();
        mapThreadLocal.remove();
    }

    @Override
    protected void onComplete(Roaster roaster, DelayQueue<Item> items) {
//        mapThreadLocal.get().put(new Item(roaster.getCode(), roaster.getExpiredAt()), items);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getComponentName() {
        return "名单加载器";
    }

    /**
     * 分钟调度，精度不用太高
     */
    @Override
    @Scheduled(fixedDelay = 1, fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public synchronized void run() {

        Map<Item, DelayQueue<Item>> local = cache;
        if (CollectionUtils.isEmpty(local)) {
            return;
        }
        Set<Item> it = local.keySet();
        for (Item i : it) {
            if (i.getDelay(TimeUnit.MILLISECONDS) <= 0) {
                local.remove(i);
            } else {
                //删除过期数据
                DelayQueue<Item> delayQueue = local.get(i);
                if (delayQueue != null) {
                    while (delayQueue.poll() != null) {

                    }
                }
                if (CollectionUtils.isEmpty(delayQueue)) {
                    local.remove(i);
                }
            }
        }
    }

    public static class Item implements Delayed {
        private String target;
        private Date expiredAt;

        public Item(String target, Date expiredAt) {
            this.target = target;
            this.expiredAt = expiredAt;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            return Objects.equal(target, item.target);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(target);
        }

        @Override
        public long getDelay(@NotNull TimeUnit unit) {
            return unit.toMillis(expiredAt == null ? Long.MAX_VALUE : expiredAt.getTime() - System.currentTimeMillis());
        }

        @Override
        public int compareTo(@NotNull Delayed o) {
            return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        }
    }
}
