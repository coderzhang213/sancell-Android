package cn.sancell.xingqiu.homecommunity.entity.res;

import java.util.List;

/**
 * Created by zj on 2019/11/28.
 */
public class LiveData {
    private int dataCount;
    private List<LiveListRes> dataList;
    private String moduleName;

    public int getDataCount() {
        return dataCount;
    }

    public void setDataCount(int dataCount) {
        this.dataCount = dataCount;
    }

    public List<LiveListRes> getDataList() {
        return dataList;
    }

    public void setDataList(List<LiveListRes> dataList) {
        this.dataList = dataList;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
