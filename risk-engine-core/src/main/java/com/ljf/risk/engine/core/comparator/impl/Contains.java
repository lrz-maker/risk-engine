package com.ljf.risk.engine.core.comparator.impl;

import com.ljf.risk.engine.core.comparator.Comparator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author lijinfeng
 * Contains
 **/
@Component
public class Contains implements Comparator {

    @Override
    public boolean compare(Object left, Object right) {
        if (left == null || right == null) {
            return false;
        }
        if (left instanceof Collection) {
            return ((Collection<?>) left).contains(right.toString());
        }
        return StringUtils.contains(left.toString(), right.toString());
    }

}
