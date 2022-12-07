package com.ljf.risk.engine.core.roster;

import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;

/**
 * 名单holder
 */
public interface RoasterHolder {
    Set<String> getRoaster(@NonNull String code);

    void refresh();

    default void refresh(@NonNull String code) {

    }

    //删缓存
    default void remove(@NonNull String code, @NonNull String value) {

    }

    default void remove(@NonNull String code, @NonNull List<String> value) {
        value.forEach(v -> remove(code, v));
    }

    boolean contains(String code, String value);
}
