package com.huoqiu.framework.rest;

public enum Configuration {
    /***************FYB Configuration**********************/
    RELEASE("http", "shopapp.iiyey.com", 0, "");

    private Configuration(String protocol, String hostname, int port, String path) {
        this.protocol = protocol;
        this.hostname = hostname;
        this.port = port;
    }

    public static Configuration DEFAULT = RELEASE;

    public String appKey = "123456";
    // 报文头需要的字段
    public String appKeyLabel = "App-Key"; // key
    public String appSecretLabel = "App-Secret"; // secret
    public String appTime = "App_Time";// 当前时间
    public String appVersion = "ver"; // 手机端版本号
    public String appOS = "os"; // 手机来源 (android/ios)
    public String appIMEI = "imei"; // 手机唯一标志
    public String appModel = "model"; // 手机型号

    // Configuration
    public String protocol = "http";
    public String hostname = "192.168.1.123";// 生产环境
    public int port = 80;
    public String path = "/rest";

    public String getRootUrl() {
        return protocol + "://" + hostname;
    }

    public String getDomain() {
        return protocol + "://" + hostname ;
    }

}