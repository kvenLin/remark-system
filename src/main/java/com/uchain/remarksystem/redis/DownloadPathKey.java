package com.uchain.remarksystem.redis;

public class DownloadPathKey extends BasePrefix{
    public DownloadPathKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static DownloadPathKey getByClientIp = new DownloadPathKey(300,"clientIp");
}
