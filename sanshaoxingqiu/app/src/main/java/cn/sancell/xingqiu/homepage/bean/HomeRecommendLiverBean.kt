package cn.sancell.xingqiu.homepage.bean

class HomeRecommendLiverBean {

    var dataList: MutableList<LiverBean> = ArrayList()

    data class LiverBean(
            var nickName: String? = null,
            var gravatar: String? = null,
            val userId: String? = null
    )

}