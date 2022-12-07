package com.ljf.risk.engine.core.comparator.impl;

import com.ljf.risk.engine.core.comparator.Comparator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author lijinfeng
 * 不等于
 **/
@Component
public class Ne implements Comparator {

    @Override
    public boolean compare(Object left, Object right) {
        if (left == null || right == null) {
            return false;
        }
        return !StringUtils.equals(left.toString(), right.toString());
    }

}
