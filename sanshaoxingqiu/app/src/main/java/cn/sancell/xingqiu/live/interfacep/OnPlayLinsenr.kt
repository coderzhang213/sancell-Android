package cn.sancell.xingqiu.live.interfacep

interface OnPlayLinsenr {
    fun onBaseStart()
    fun onBaseStop()

    /**
     * 获取当前播放状态
     */
    fun onBasePlayStates(): PLAY_STATE
    fun onLiveEndPlay()
    fun onSetPlayStatus(isPlay: Boolean)
}