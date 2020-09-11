package cn.sancell.xingqiu.bean;

public class UpIdCardBean {

    public String json;

    public int type;

    public String backName;
    public String endpoint;
    public String accessKeyId;
    public String accessKeySecret;
    public String expiration;
    public String securityToken;
    public String keyName;

    public UpIdCardBean(String json, int type) {
        this.json = json;
        this.type = type;
    }
}
