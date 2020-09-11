package cn.sancell.xingqiu.viewGroup.manager;


import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.sancell.xingqiu.ProducAddressInfo;
import cn.sancell.xingqiu.homeshoppingcar.bean.HomeShoppingCarDataBean;
import cn.sancell.xingqiu.homeuser.bean.AddressListDataBean;
import cn.sancell.xingqiu.viewGroup.entity.AreaSupportModel;

/**
 * @author Alan_Xiong
 * @desc: 商品配送区域管理
 * @time 2019-12-13 15:42
 */
public class GoodsAreaManger {

    private static final class Holder {
        private static final GoodsAreaManger instance = new GoodsAreaManger();
    }

    public static GoodsAreaManger getInstance() {
        return Holder.instance;
    }


    /**
     * 判断商品是否在配送区域内
     *
     * @param provinceId
     * @param datas
     * @return
     */
    public AreaSupportModel getAreaSupport(String provinceId, @NonNull List<HomeShoppingCarDataBean.ShopingCarProductBean> datas) {
        AreaSupportModel model = new AreaSupportModel();
        model.totalCount = datas.size();
        model.vailList = new ArrayList<>();
        model.inVailList = new ArrayList<>();
        if (TextUtils.isEmpty(provinceId)) {
            model.vailList = datas;
            return model;
        }
        for (HomeShoppingCarDataBean.ShopingCarProductBean item : datas) {
            if (isGoodsInProvince(provinceId, item)) {
                model.vailList.add(item);
            } else {
                model.inVailList.add(item);
            }
        }

        return model;
    }


    /**
     * 商品是否在当前省份配送范围
     *
     * @param provinceId
     * @param item
     * @return
     */
    private boolean isGoodsInProvince(String provinceId, HomeShoppingCarDataBean.ShopingCarProductBean item) {

        if (item.provinceIdList != null && item.provinceIdList.size() > 0) {
            return item.provinceIdList.contains(provinceId);
        }
        return false;


    }

    /**
     * 获取排序后的商品列表
     *
     * @param provinceId
     * @param datas
     * @return
     */
    public AreaSupportModel getSortGoodsList(String provinceId, @NonNull List<HomeShoppingCarDataBean.ShopingCarProductBean> datas) {
        AreaSupportModel model = getAreaSupport(provinceId, datas);
        if (model.vailList != null && model.vailList.size() > 1) {
            Comparator<HomeShoppingCarDataBean.ShopingCarProductBean> comparator = (o1, o2) -> {
                int result = o2.getSupplierId() - o1.getSupplierId(); // 供应商id按降序
                return result;
            };
            Collections.sort(model.vailList, comparator);
        }
        return model;
    }


    /**
     * 单个商品是否在配送区域内
     *
     * @param provinceId
     * @param provinceList
     * @return
     */
    public boolean singleGoodsInArea(String provinceId, List<String> provinceList) {
        if (TextUtils.isEmpty(provinceId)) {
            return true;
        }
        if (provinceList != null && provinceList.size() > 0) {
            return provinceList.contains(provinceId);
        } else {
            return false;
        }

    }

    /**
     * 获取总价
     *
     * @param model
     * @return
     */
    public int[] getTotalPrice(@NonNull AreaSupportModel model) {

        int total = 0;
        int sharePrice = 0;
        if (model.vailList != null && model.vailList.size() > 0) {
            for (HomeShoppingCarDataBean.ShopingCarProductBean item : model.vailList) {
                total += item.getUserRealPriceE2() * item.getGoodsNum();
                sharePrice += item.getUserRealAllProfitSharingE2() * item.getGoodsNum();
            }
        }
        return new int[]{total,sharePrice};
    }

    /**
     * 商品详情 获取当前地址是否在配送区
     * {@link cn.sancell.xingqiu.goods.GoodsDetailActivity}
     * @param info
     * @param provinceId
     * @return
     */
    public boolean getGoodsInArea(ProducAddressInfo info,String provinceId){
        if (TextUtils.isEmpty(provinceId)){
            return true;
        }
        if (info != null && info.getProvinceIdList() != null){
            return info.getProvinceIdList().contains(provinceId);
        }else{
            return false;
        }
    }

    public AddressListDataBean.AddressItemBean getGoodsAreaItemById(@NonNull AddressListDataBean res, String areaId){

        for (AddressListDataBean.AddressItemBean item:res.getDataList()){
            if (TextUtils.equals(item.getId()+"",areaId)){
                return item;
            }
        }
        return null;
    }

}


