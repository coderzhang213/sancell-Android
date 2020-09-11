package cn.sancell.xingqiu.bean

data class LiveInitData(val batchId: String, val isUse: String
        /**
        是否弹出复用提示；1：弹出；0不需要弹出**/
                        , val hisData: LiveIntitParInfo?

) {
}