package cn.sancell.xingqiu;

import java.util.List;

/**
 * Created by zj on 2019/12/13.
 */
public class ProducAddressInfo {
    private List<String> provinceIdList;
    private String regionAlias;//配送区域简洁：只发江浙沪
    private String provinceName;//配送区域详细：上海，浙江，江苏

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public List<String> getProvinceIdList() {
        return provinceIdList;
    }

    public void setProvinceIdList(List<String> provinceIdList) {
        this.provinceIdList = provinceIdList;
    }

    public String getRegionAlias() {
        return regionAlias;
    }

    public void setRegionAlias(String regionAlias) {
        this.regionAlias = regionAlias;
    }
}
