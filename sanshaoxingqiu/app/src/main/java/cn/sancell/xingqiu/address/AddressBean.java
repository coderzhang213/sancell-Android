package cn.sancell.xingqiu.address;

import java.util.List;

/**
 * Created by ZhouZi on 2018/9/29.
 * time:11:24
 * ----------Dragon be here!----------/
 * 　　　┏┓　　 ┏┓
 * 　　┏┛┻━━━┛┻┓━━━
 * 　　┃　　　　　 ┃
 * 　　┃　　　━　  ┃
 * 　　┃　┳┛　┗┳
 * 　　┃　　　　　 ┃
 * 　　┃　　　┻　  ┃
 * 　　┃　　　　   ┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽保佑,代码无bug
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛━━━━━
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━━━━━━神兽出没━━━━━━━━━━━━━━
 */
public class AddressBean {

    private String name;
    private String code;
    private boolean status;
    private List<CityBean> children;

    public String getLabel() {
        return name;
    }

    public void setLabel(String label) {
        this.name = label;
    }

    public String getValue() {
        return code;
    }

    public void setValue(String value) {
        this.code = value;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<CityBean> getChildren() {
        return children;
    }

    public void setChildren(List<CityBean> children) {
        this.children = children;
    }

    public class CityBean {
        private String name;
        private String code;
        private boolean status;
        private List<AreaBean> children;

        public String getLabel() {
            return name;
        }

        public void setLabel(String label) {
            this.name = label;
        }

        public String getValue() {
            return code;
        }

        public void setValue(String value) {
            this.code = value;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public List<AreaBean> getChildren() {
            return children;
        }

        public void setChildren(List<AreaBean> children) {
            this.children = children;
        }

        public class AreaBean {
            private String name;
            private String code;
            private boolean status;
            private List<TownBean> children;

            public String getLabel() {
                return name;
            }

            public void setLabel(String label) {
                this.name = label;
            }

            public String getValue() {
                return code;
            }

            public void setValue(String value) {
                this.code = value;
            }

            public boolean isStatus() {
                return status;
            }

            public void setStatus(boolean status) {
                this.status = status;
            }

            public List<TownBean> getChildren() {
                return children;
            }

            public void setChildren(List<TownBean> children) {
                this.children = children;
            }

            public class TownBean {
                private String name;
                private String code;
                private boolean status;

                public String getLabel() {
                    return name;
                }

                public void setLabel(String label) {
                    this.name = label;
                }

                public String getValue() {
                    return code;
                }

                public void setValue(String value) {
                    this.code = value;
                }

                public boolean isStatus() {
                    return status;
                }

                public void setStatus(boolean status) {
                    this.status = status;
                }
            }
        }


    }

}
