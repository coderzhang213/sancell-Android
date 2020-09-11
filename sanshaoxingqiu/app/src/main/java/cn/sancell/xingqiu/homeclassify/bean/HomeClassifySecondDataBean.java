package cn.sancell.xingqiu.homeclassify.bean;

import java.io.Serializable;
import java.util.List;

import cn.sancell.xingqiu.base.base.ListBaseBean;

/**
 * Created by ai11 on 2019/6/10.
 */

public class HomeClassifySecondDataBean extends ListBaseBean {
    private List<ClassifySecondBean> dataList;

    public List<ClassifySecondBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<ClassifySecondBean> dataList) {
        this.dataList = dataList;
    }

    public  class ClassifySecondBean {
        private int id;
        private String typeName;
        private HomeClassifyThirdDataBean sonTypeInfo;

        public HomeClassifyThirdDataBean getSonTypeInfo() {
            return sonTypeInfo;
        }

        public void setSonTypeInfo(HomeClassifyThirdDataBean sonTypeInfo) {
            this.sonTypeInfo = sonTypeInfo;
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

        public class HomeClassifyThirdDataBean extends ListBaseBean {
            private List<ClassifyThirdBean> dataList;

            public List<ClassifyThirdBean> getDataList() {
                return dataList;
            }

            public void setDataList(List<ClassifyThirdBean> dataList) {
                this.dataList = dataList;
            }

            public  class ClassifyThirdBean implements Serializable {
                private int id;
                private String typeName;
                private String coverPic;

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

                public String getPic() {
                    return coverPic;
                }

                public void setPic(String pic) {
                    this.coverPic = pic;
                }
            }
        }

    }
}
