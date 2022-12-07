package com.ljf.risk.engine.api;

public class PlayLoadResponse<T> extends Response {
    private T playLoad;

    public static <T> PlayLoadResponse<T> success(T playLoad) {
        return new PlayLoadResponse(playLoad);
    }

    public static <T> PlayLoadResponse<T> failurePlayLoad(String code, String msg) {
        return new PlayLoadResponse(false, code, msg, (Object)null);
    }

    public PlayLoadResponse(boolean success, String code, String msg, T playLoad) {
        super(success, code, msg);
        this.playLoad = playLoad;
    }

    public PlayLoadResponse(T playLoad) {
        super(true, (String)null, (String)null);
        this.playLoad = playLoad;
    }

    public PlayLoadResponse() {
    }

    public T getPlayLoad() {
        return this.playLoad;
    }

    public void setPlayLoad(T playLoad) {
        this.playLoad = playLoad;
    }
}
