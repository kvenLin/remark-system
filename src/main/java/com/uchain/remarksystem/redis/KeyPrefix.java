package com.uchain.remarksystem.redis;

public interface KeyPrefix {
    public int expireSeconds();
    public String getPrefix();
}
