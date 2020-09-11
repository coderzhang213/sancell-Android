package cn.sancell.xingqiu.live.bean;

import java.util.List;

/**
 * Created by zj on 2019/11/28.
 */
public class LiveCommentInfo {
    private int dataCount;
    private String dataList;//因为可能给两种数据对象
    private String retHtml;

    public int getDataCount() {
        return dataCount;
    }

    public void setDataCount(int dataCount) {
        this.dataCount = dataCount;
    }

    public String getDataList() {
        return dataList;
    }

    public void setDataList(String dataList) {
        this.dataList = dataList;
    }

    public String getRetHtml() {
        return retHtml;
    }

    public void setRetHtml(String retHtml) {
        this.retHtml = retHtml;
    }
}
