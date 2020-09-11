package cn.sancell.xingqiu.homepage.bean;

import java.util.List;

/**
 * Created by zj on 2020/1/8.
 */
public class SerchBaseData {
    private List<KeyBean> data;
    private String defaultName;

    public List<KeyBean> getData() {
        return data;
    }

    public void setData(List<KeyBean> data) {
        this.data = data;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }
}
