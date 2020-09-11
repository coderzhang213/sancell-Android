package cn.sancell.xingqiu.homeclassify.bean;

import java.io.Serializable;

/**
 * Created by ai11 on 2019/6/27.
 */

public class ClassifyChannelBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6465237897027410019L;
    private int id;
    private String typeName;

    public ClassifyChannelBean(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return typeName;
    }

    public void setName(String typeName) {
        this.typeName = typeName;
    }
}
