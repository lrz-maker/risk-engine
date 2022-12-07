package com.ljf.risk.engine.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * p1=整数 | "(" p3 ")"
 * <p>
 * p2=  p1 {"*" p1}
 * <p>
 * p3 =  p2 {"+" p2}
 */
public class EBNFParser {

    Integer p1(Object[] c, AtomicInteger index) {
        //p1=整数 | "(" p3 ")"
        if (index.get() >= c.length) {
            throw new RuntimeException();
        }
        Object ob = c[index.getAndIncrement()];
        if (ob instanceof Integer) {
            return (Integer) ob;
        } else if ("(".equals(ob)) {
            Integer integer = p3(c, index);
            if (")".equals(c[index.getAndIncrement()])) {
                return integer;
            }
            throw new RuntimeException("expect )");
        }
        throw new RuntimeException("expect number or ( expr ) ");
    }

    Integer p2(Object[] c, AtomicInteger index) {
        //p2=  p1 {"*" p1}
        if (index.get() >= c.length) {
            throw new RuntimeException();
        }
        Integer left = p1(c, index);
        while (index.get() < c.length && "*".equals(c[index.get()])) {
            index.incrementAndGet();
            left *= p1(c, index);
        }
        return left;
    }

    Integer p3(Object[] c, AtomicInteger index) {
        //p3 =  p2 {"+" p2}
        if (index.get() >= c.length) {
            throw new RuntimeException();
        }
        Integer left = p2(c, index);
        while (index.get() < c.length && "+".equals(c[index.get()])) {
            index.incrementAndGet();
            left += p2(c, index);
        }
        return left;
    }

    public static void main(String[] args) {
        Object[] objects = new Object[9];
        //1&&2||3
        objects[0] = 1;
        objects[1] = "*";
        objects[2] = "(";
        objects[3] = 2;
        objects[4] = "+";
        objects[5] = 3;
        objects[6] = ")";
        objects[7] = "*";
        objects[8] = 4;
        EBNFParser ebnfParser = new EBNFParser();
        System.out.println(ebnfParser.p3(objects, new AtomicInteger(0)));
    }
}
