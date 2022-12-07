package com.ljf.risk.engine.core.comparator.impl;

import com.ljf.risk.engine.core.comparator.Comparator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author lijinfeng
 * 小于等于
 **/
@Component
public class Le implements Comparator {

    @Override
    public boolean compare(Object left, Object right) {
        if (left == null || right == null) {
            return false;
        }
        BigDecimal l = (BigDecimal) left;
        BigDecimal r = (BigDecimal) right;
        return l.compareTo(r) <= 0;
    }

}
