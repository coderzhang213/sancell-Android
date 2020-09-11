package cn.sancell.xingqiu.homeclassify.bean;

import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/6/10.
 */

public class HomeClassifyFirstDataBean extends ListBaseBean {
    private List<ClassifyFirstListBean> dataList;

    public List<ClassifyFirstListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<ClassifyFirstListBean> dataList) {
        this.dataList = dataList;
    }

    public static class ClassifyFirstListBean {
        private int id;
        private String typeName;
        private boolean isSelect;
        private String moduleId;
        private String coverPic;

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getModuleId() {
            return moduleId;
        }

        public void setModuleId(String moduleId) {
            this.moduleId = moduleId;
        }

        public String getCoverPic() {
            return coverPic;
        }

        public void setCoverPic(String coverPic) {
            this.coverPic = coverPic;
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

        public void setName(String name) {
            this.typeName = name;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
    }

}
