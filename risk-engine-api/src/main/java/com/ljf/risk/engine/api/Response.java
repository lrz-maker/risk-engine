//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ljf.risk.engine.api;

public class Response {
    private boolean success;
    private String code;
    private String msg;
    private static Response internal = new Response(true, (String)null, (String)null);

    public static Response success() {
        return internal;
    }

    public static Response failure(String code, String msg) {
        assert code != null;

        return new Response(false, code, msg);
    }

    public Response(boolean success, String code, String msg) {
        this.success = success;
        this.code = code;
        this.msg = msg;
    }

    public Response() {
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        if (success && this.code != null) {
            this.code = null;
            this.msg = null;
        }

        this.success = success;
    }

    public String getCode() {
        return this.success ? null : this.code;
    }

    public void setCode(String code) {
        if (code != null) {
            this.success = false;
        }

        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
