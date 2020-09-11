package cn.sancell.xingqiu.homeuser.repository

import cn.sancell.xingqiu.bean.CouponInfo
import cn.sancell.xingqiu.bean.DefalutResultInfo
import cn.sancell.xingqiu.bean.VoucherCenterRes
import handbank.hbwallet.BaseRepository
import handbank.hbwallet.ResResponse

class VoucherRepository : BaseRepository() {

    suspend fun getVoucherList(params: Map<String, String>): ResResponse<VoucherCenterRes> {
        return mServe.getVoucherCenter(params)
    }

    //直播间优惠券列表
    suspend fun getLiveRoomCoup(params: Map<String, String>): ResResponse<CouponInfo> {
        return mServe.getLiveRoomCoup(params)
    }

    suspend fun receiveLiveRoomCoupon(params: Map<String, String>): ResResponse<DefalutResultInfo> {
        return mServe.receiveLiveRoomCoupon(params)
    }
    suspend fun addStock(params: Map<String, String>): ResResponse<DefalutResultInfo> {
        return mServe.addStock(params)
    }

}