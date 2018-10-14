package com.uchain.remarksystem.redis;

public class VerifyCodeKey extends BasePrefix {

    public VerifyCodeKey(String prefix) {
        super(prefix);
    }

    public VerifyCodeKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static VerifyCodeKey getByClientIp = new VerifyCodeKey(240,"clientIp");
}
