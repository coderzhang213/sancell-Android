package cn.sancell.xingqiu.bean

/**
 * Created by zj on 2020/5/13.
 */
data class CouponItemInfo(var id: String = "",
                          var couponCode: String = "",
                          var name: String = "",
                          var desc: String = "",
                          var getNumTimes: String = "",
                          var getUserType: String = "",//1:全部用户 1：新用户（7天内注册）
                          var useLimitTimes: String = "",//每笔订单使用限制
                          var useType: String = "",//1：全品类 2：部分商品
                          var sendType: String = "",//发放方式 1.直播自动发放
                          var usedTimeType: String = "",//使用时间类型 1：领劵后多少天 2：固定周期
                          var receiveLimitUseDay: String = "",//领取后限制的使用天数
                          var fixedUseBeginTime: Long = 0L,//优惠券领取后使用的开始时间
                          var fixedUseEndTime: Long = 0L,//优惠券领取后使用的结束时间
                          var type: String = "",//优惠劵类型 1：立减 2：满减 3：折扣
                          var faceValueE2: String = "",//面值e2*100
                          var limitMinUseMoneyE2: Long = 0L,//限制最小的面值使用金额 （满减时存在）
                          var discountE2: String = "",//折扣值 E2*100
                          var status: String = "",//1.待审核 2.审核失败 3.未发放（审核成功）4.已发放 5.停用6.停发
                          var sendTime: String = "",//发放时间
                          var showStatus: String = ""//1可领取；2已领过；3被抢完


) {
}