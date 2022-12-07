package com.ljf.risk.engine.core.fuction.json;

import com.alibaba.fastjson.JSONPath;
import com.ljf.risk.engine.entity.Function;
import com.ljf.risk.engine.core.fuction.AbstractFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lijinfeng
 */
@Slf4j
@Component
public class GetValueByJsonPath extends AbstractFunction {

    @Override
    public Object doService(Map<String, Object> context, String params) {
        return JSONPath.eval(context, params);
    }

    @Override
    public Function getModel() {
        return Function.builder()
                .code(StringUtils.uncapitalize(this.getClass().getSimpleName()))
                .description("通过JSONPATH获取上下文中的值")
                .returnType(Function.ReturnType.OBJECT)
                .build();
    }


}
