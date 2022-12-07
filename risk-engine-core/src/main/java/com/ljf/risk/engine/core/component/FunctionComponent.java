package com.ljf.risk.engine.core.component;

import com.ljf.risk.engine.core.SpringContextComponent;
import com.ljf.risk.engine.core.component.load.AbstractComponent;
import com.ljf.risk.engine.core.fuction.CustomFunction;
import com.ljf.risk.engine.entity.Function;
import com.ljf.risk.engine.service.FunctionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lijinfeng
 */
@Slf4j
@Component
public class FunctionComponent extends AbstractComponent<String, CustomFunction> {

    private final SpringContextComponent springContextComponent;

    private final FunctionService functionService;

    public FunctionComponent(SpringContextComponent springContextComponent, FunctionService functionService) {
        this.springContextComponent = springContextComponent;
        this.functionService = functionService;
    }

    @Override
    public String getComponentName() {
        return "内置函数";
    }

    @Override
    public void load() {
        List<Function> functions = new ArrayList<>();
        Map<String, CustomFunction> beans = springContextComponent.getBeans(CustomFunction.class);
        ConcurrentHashMap<String, CustomFunction> temp = new ConcurrentHashMap<>();

        for (Map.Entry<String, CustomFunction> function : beans.entrySet()) {
            CustomFunction value = function.getValue();
            Function model = value.getModel();
            functions.add(model);
            temp.put(function.getKey(), function.getValue());
        }

        List<Function> list = functionService.list();
        List<Function> toRemove = new ArrayList<>(list);

        toRemove.removeAll(functions);
        if (!CollectionUtils.isEmpty(toRemove)) {
            functionService.removeBatchByIds(toRemove);
        }

        functions.removeAll(list);
        if (!CollectionUtils.isEmpty(functions)) {
            functionService.saveBatch(functions);
        }

        this.cache = temp;
    }

    public CustomFunction getCacheByFunctionCode(String functionCode) {
        return cache.get(functionCode);
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
