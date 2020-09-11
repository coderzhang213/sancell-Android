package cn.sancell.xingqiu.live.play

class LivePlayStatusManager {
    //播放列表在首页的哪个位置 0 或者1
    private val PLAY_LIST_INDEX = 1

    //首页显示的界面索引 （首页0 个人1）
    private var mHomeShowIndex = PLAY_LIST_INDEX

    //播放界面显示索引(推荐0  关注1)
    private var mPlayBaseShowIndex = 1

    //播放滑动上下列表显示的索引
    private var mCurrShowIndoex = 0

    //当前LivePlayBaseFragment界面是否可见
    private var mPagerShow = true

    // 是否进入了输入界面
    private var isShowInputMsg = false


    fun setisShowInputMsg(isShowInputMsg: Boolean) {
        this.isShowInputMsg = isShowInputMsg
    }

    fun setPagerShow(mPagerShow: Boolean) {
        this.mPagerShow = mPagerShow
    }

    fun setHomeShowIndex(index: Int) {
        mHomeShowIndex = index
    }

    fun setPlayBaseShowIndex(index: Int) {
        mPlayBaseShowIndex = index
    }

    fun getPlayBaseShowIndex() = mPlayBaseShowIndex

    fun setCurrShowIndoex(index: Int) {
        mCurrShowIndoex = index
    }

    /**
     * 判断当前界面是否处于可见
     */
    fun currPageIsShow(postion: Int): Boolean {
        if (mPagerShow && mHomeShowIndex == PLAY_LIST_INDEX  && postion == mCurrShowIndoex) {
            return true
        }
        return false
    }

    /**
     * 关注界面是否可见
     */
    fun attentionIsShow(): Boolean {
        if (mPlayBaseShowIndex == 1) {
            return true
        }
        return false
    }

    /**
     * 个人中心是否可见
     */
    fun personagePagerIsShow(): Boolean {
        if (mHomeShowIndex == 1) {
            return true
        }
        return false
    }

    fun getCurrShowIndoex(): Int = mCurrShowIndoex
}