package com.uchain.remarksystem.redis;

public class RowStatusKey extends BasePrefix{

    public RowStatusKey(String prefix) {
        super(prefix);
    }

    public RowStatusKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static RowStatusKey getByPackageId = new RowStatusKey("packageId");

}
