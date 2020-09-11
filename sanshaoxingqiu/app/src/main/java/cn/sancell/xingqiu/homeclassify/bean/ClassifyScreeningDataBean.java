package cn.sancell.xingqiu.homeclassify.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.BaseBean;
import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/6/12.
 */

public class ClassifyScreeningDataBean extends ListBaseBean {
    private List<ScreeningItemBean> dataList;

    public List<ScreeningItemBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<ScreeningItemBean> dataList) {
        this.dataList = dataList;
    }

    public class ScreeningItemBean {
        private String tagName;
        private int id;
        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getName() {
            return tagName;
        }

        public void setName(String name) {
            this.tagName = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
