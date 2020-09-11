package cn.sancell.xingqiu.homepage.bean

/**
 * Created by zj on 2019/12/26.
 */
data class HomeMenuInfo(val dataCount: Int, val dataList: MutableList<MenuInfo>) {
    data class MenuInfo(
            var id: String? = null,
            var desc: String? = null,
            var status: String? = null,//
            val name: String,
            var coverPic: String? = null,
            var show: Int? = null//是否显示 1显示 2不显示
    )
}