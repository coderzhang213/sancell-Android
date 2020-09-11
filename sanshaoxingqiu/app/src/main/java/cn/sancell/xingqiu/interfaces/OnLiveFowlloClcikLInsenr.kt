package cn.sancell.xingqiu.interfaces

import cn.sancell.xingqiu.bean.LiveFollowInfo

interface OnLiveFowlloClcikLInsenr {
    fun onItemClickLinsener(data: LiveFollowInfo)
    fun onMakeLinser(type: String, data: LiveFollowInfo)
}