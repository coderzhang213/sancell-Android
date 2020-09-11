package cn.sancell.xingqiu.bean

/**
 * Created by zj on 2020/6/18.
 */
data class IncomeParInfo(var canWithdraw: Long = 0,
                         var sumIncome: Long = 0,
                         var withdrawCash: Long = 0,
                         var withdrawing: Long = 0,
                         var incomeList: IncomeList? = null

) {
}