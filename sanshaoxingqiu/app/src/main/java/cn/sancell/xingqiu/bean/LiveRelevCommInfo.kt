package cn.sancell.xingqiu.bean

data class LiveRelevCommInfo(val title: String,
                             val marketPriceE2: String,
                             val sellingPriceE2: Long,
                             val newMarketPriceE2: Long,
                             val coverPicThumb: String,
                             val minPriceE2: String,
                             val userRealPriceE2: String,
                             val goodsId: String,
                             val sku: String,
                             val isActivity: String,
                             val titleAlias: String,

                             /** 群组参数**/
                             val id: String,
                             val tid: String,
                             val groupName: String,
                             val icon: String,
                             val intro: String,
                             val inGroup: Int,
                             /** // 1在 0不再**/
                             var ownerUserName: String? = ""

) {
}