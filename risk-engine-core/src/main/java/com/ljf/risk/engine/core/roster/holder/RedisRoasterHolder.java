package com.ljf.risk.engine.core.roster.holder;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljf.risk.engine.core.roster.RoasterHolder;
import com.ljf.risk.engine.entity.Roaster;
import com.ljf.risk.engine.entity.RoasterList;
import com.ljf.risk.engine.service.RoasterListService;
import com.ljf.risk.engine.service.RoasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 适用日活小于名单数量,或者用户的活跃有明显的时间聚集性
 *
 * @author weiweihan
 */
@Component
@Slf4j
public class RedisRoasterHolder extends Thread implements RoasterHolder, InitializingBean, DisposableBean {
    private final StringRedisTemplate redisTemplate;
    private static final String namespace = "roaster:";
    private final RoasterService roasterService;
    private final RoasterListService roasterListService;
    private static final String NONE = "0";
    private final BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
    private boolean started = true;
    //调整duration的值来观察cache miss的情况
    private final Duration duration = Duration.ofHours(48);
    private final AtomicLong accessCount = new AtomicLong(0);
    private final AtomicLong hitCount = new AtomicLong(0);

    public RedisRoasterHolder(RoasterService roasterService, RoasterListService roasterListService, StringRedisTemplate redisTemplate) {
        this.roasterService = roasterService;
        this.roasterListService = roasterListService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Set<String> getRoaster(String code) {
        throw new RuntimeException();
    }

    @Override
    public void refresh() {
    }

    @Override
    public boolean contains(String code, String value) {
        accessCount.incrementAndGet();
        String key = getKey(code, value);
        String h = redisTemplate.opsForValue().get(key);
        if (h == null) {
            String v = NONE;
            boolean x = false;
            Roaster roaster = roasterService.getOne(new LambdaQueryWrapper<Roaster>().eq(Roaster::getCode, code));
            if (Objects.nonNull(roaster)) {
                RoasterList roasterList = roasterListService.getOne(new LambdaQueryWrapper<RoasterList>().eq(RoasterList::getRoasterId, roaster.getId()).eq(RoasterList::getValue, value));
                if (Objects.nonNull(roasterList)) {
                    Date expiredAt = roasterList.getExpiredAt();
                    x = expiredAt == null || new Date().before(expiredAt);
                    v = (expiredAt == null ? Long.MAX_VALUE : expiredAt.getTime()) + "";
                }
            }
            redisTemplate.opsForValue().set(key, v, duration);
            return x;
        }
        hitCount.incrementAndGet();
        //延长时间
        renew(key);
        return Long.parseLong(h) - System.currentTimeMillis() > 0;
    }

    protected String getKey(String code, String value) {
        return namespace + code + ":" + value;
    }

    @Override
    public void remove(String code, String value) {
        redisTemplate.delete(getKey(code, value));
    }

    @Override
    public void afterPropertiesSet() {
        this.start();
    }

    /**
     * 续约
     */
    protected void renew(String key) {
        blockingQueue.add(key);
    }

    @Override
    public void run() {
        List<String> l = new ArrayList<>();
        long lastFlushTime = System.currentTimeMillis();
        while (started) {
            try {
                String key = blockingQueue.poll(5, TimeUnit.MINUTES);
                if (key != null) {
                    if (l.size() == 0) {
                        lastFlushTime = System.currentTimeMillis();
                    }
                    l.add(key);
                }
                if ((l.size() > 0 && TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - lastFlushTime) > 5) || l.size() >= 50) {
                    doBatchFlush(l);
                    lastFlushTime = System.currentTimeMillis();
                    //统计cache miss
                    long h = hitCount.get();
                    long c = accessCount.get();
                    if (c != 0) {
                        DecimalFormat decimalFormat = new DecimalFormat("0.00%");
                        log.info("cache miss: {}  access:{} hit:{}", decimalFormat.format((float) (c - h) / c), c, h);
                    }
                }
            } catch (Exception ex) {
                log.info("ex", ex);
            }
        }
        blockingQueue.drainTo(l);
        doBatchFlush(l);
    }

    private void doBatchFlush(List<String> l) {
        if (l.size() > 0) {
            redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                l.removeIf(p -> {
                    connection.expire(p.getBytes(StandardCharsets.UTF_8), duration.getSeconds());
                    return true;
                });
                return null;
            });
        }
    }

    @Override
    public void destroy() {
        started = false;
    }

    @Override
    public void remove(String code, List<String> value) {
        List<String> delKeys = new ArrayList<>(value.size());
        value.forEach(v -> delKeys.add(getKey(code, v)));
        redisTemplate.delete(delKeys);
    }
}

