package cn.sancell.xingqiu.interfaces

import cn.sancell.xingqiu.bean.RecomLiveInfo

/**
 * Created by zj on 2020/3/18.
 */
interface OnAddRecommendLinsener {
    fun onAddRecommendData(dataList: List<RecomLiveInfo>?)
}