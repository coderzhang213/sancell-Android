package cn.sancell.xingqiu.homepage.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.BaseBean;
import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/6/8.
 */

public class HomePageLikeListDataBean extends BaseBean {
    private String moduleName;
    private LikeListDataBean likeData;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public LikeListDataBean getLikeData() {
        return likeData;
    }

    public void setLikeData(LikeListDataBean likeData) {
        this.likeData = likeData;
    }

    public class LikeListDataBean extends ListBaseBean {
        private List<LikeBean> dataList;

        public List<LikeBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<LikeBean> dataList) {
            this.dataList = dataList;
        }

    }
}
