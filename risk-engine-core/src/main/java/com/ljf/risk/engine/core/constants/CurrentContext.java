package com.ljf.risk.engine.core.constants;

import com.ljf.risk.engine.api.EngineApi;
import com.ljf.risk.engine.entity.Event;
import com.ljf.risk.engine.entity.Rule;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lijinfeng
 */
public class CurrentContext implements AutoCloseable {

    private final static ThreadLocal<Context> CTX = new ThreadLocal<>();

    public CurrentContext(Context context) {
        CTX.set(context);
    }

    public static Context currentCtx() {
        return CTX.get();
    }

    public static Date getEventTime() {
        return CTX.get().getEventTime();
    }

    @Override
    public void close() {
        CTX.remove();
    }

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Context {

        private EngineApi.EngineCheckReq engineCheckReq;

        private Event event;

        private Map<String, Object> functionResult;

        private Map<String, Object> conditionResult;

        private List<String> hitRuleNames;

        private Rule hitRule;

        private Date eventTime;

        private Map<String, Object> indicatorResult;

        public String getEventCode() {
            if (event == null) {
                return null;
            }
            return event.getCode();
        }

        public String getEventCodeDesc() {
            if (event == null) {
                return null;
            }
            return event.getDescription();
        }

    }
}
