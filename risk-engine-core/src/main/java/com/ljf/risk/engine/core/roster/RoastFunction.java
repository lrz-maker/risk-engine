package com.ljf.risk.engine.core.roster;

import com.ljf.risk.engine.core.constants.CurrentContext;
import com.ljf.risk.engine.core.fuction.CustomFunction;
import com.ljf.risk.engine.entity.Function;
import com.ljf.risk.engine.entity.FunctionExtend;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 名单函数
 */
@Component
public class RoastFunction implements CustomFunction {
    private RoasterHolder roasterHolder;

    public RoastFunction(RoasterHolder roasterHolder) {
        this.roasterHolder = roasterHolder;
    }

    private final Function description = Function.builder()
            .code(StringUtils.uncapitalize(this.getClass().getSimpleName()))
            .description("名单函数")
            .returnType(Function.ReturnType.COLLECTION)
            .build();

    /**
     * 不直接返回原始数据，避免过多的数据交换；
     * 如redis，避免全量返回数据
     */
    @Override
    public Collection<String> execute(CurrentContext.Context context, FunctionExtend functionExtend) {
        String code = functionExtend.getParams();

        return new ArrayList<String>() {
            @Override
            public boolean contains(Object o) {
                return roasterHolder.contains(code, (String) o);
            }

            @Override
            public String toString() {
                return code;
            }
        };

//        return new Collection<String>() {
//
//            @Override
//            public String toString() {
//                return code;
//            }
//
//            @Override
//            public int size() {
//                return 0;
//            }
//
//            @Override
//            public boolean isEmpty() {
//                throw new UnsupportedOperationException();
//            }
//
//            @Override
//            public boolean contains(Object o) {
//                return roasterHolder.contains(code, (String) o);
//            }
//
//            @NotNull
//            @Override
//            public Iterator<String> iterator() {
//                return new Iterator<String>() {
//                    @Override
//                    public boolean hasNext() {
//                        return false;
//                    }
//
//                    @Override
//                    public String next() {
//                        return code;
//                    }
//                };
//            }
//
//            @NotNull
//            @Override
//            public Object[] toArray() {
//                throw new UnsupportedOperationException();
//            }
//
//            @NotNull
//            @Override
//            public <T> T[] toArray(@NotNull T[] a) {
//                throw new UnsupportedOperationException();
//            }
//
//            @Override
//            public boolean add(String s) {
//                throw new UnsupportedOperationException();
//            }
//
//            @Override
//            public boolean remove(Object o) {
//                throw new UnsupportedOperationException();
//            }
//
//            @Override
//            public boolean containsAll(@NotNull Collection<?> c) {
//                throw new UnsupportedOperationException();
//            }
//
//            @Override
//            public boolean addAll(@NotNull Collection<? extends String> c) {
//                throw new UnsupportedOperationException();
//            }
//
//            @Override
//            public boolean removeAll(@NotNull Collection<?> c) {
//                throw new UnsupportedOperationException();
//            }
//
//            @Override
//            public boolean retainAll(@NotNull Collection<?> c) {
//                throw new UnsupportedOperationException();
//            }
//
//            @Override
//            public void clear() {
//                throw new UnsupportedOperationException();
//            }
//        };
    }

    @Override
    public Boolean precondition(FunctionExtend functionExtend, CurrentContext.Context context) {
        return null;
    }

    @Override
    public Function getModel() {
        return description;
    }
}
