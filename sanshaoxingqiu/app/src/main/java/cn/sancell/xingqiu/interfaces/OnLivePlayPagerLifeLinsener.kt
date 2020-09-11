package cn.sancell.xingqiu.interfaces

interface OnLivePlayPagerLifeLinsener {
    fun onLiveResume()
    fun onLivePause()
    fun onLiveDestroy()
    fun onLivePostion(postion: Int)
}