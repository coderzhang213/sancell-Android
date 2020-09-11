package cn.sancell.xingqiu.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.sancell.xingqiu.Repository.IncomeRepository
import cn.sancell.xingqiu.Repository.LiveUserRepository
import cn.sancell.xingqiu.base.ConvertUtils
import cn.sancell.xingqiu.bean.DefalutResultInfo
import cn.sancell.xingqiu.bean.FansRes
import cn.sancell.xingqiu.bean.IncomeParInfo
import cn.sancell.xingqiu.bean.UserLevelInfo
import handbank.hbwallet.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by zj on 2020/6/18.
 */
class IncomeViewModel : BaseViewModel() {
    val mIncomeRepository by lazy { IncomeRepository() }
    val mLiveUserRepository by lazy { LiveUserRepository() }

    val mAddBnakRes = MutableLiveData<DefalutResultInfo>()
    val mWidt = MutableLiveData<DefalutResultInfo>()
    val mIncome = MutableLiveData<IncomeParInfo>()

    //添加银行卡
    fun addBnakCard(bankName: String, cardNum: String, cardUserName: String) {
        val par = ConvertUtils.getRequest()
        par["bankName"] = bankName
        par["cardNum"] = cardNum
        par["cardUserName"] = cardUserName
        launch {
            val response = withContext(Dispatchers.IO) {
                mIncomeRepository.addBankCard(par)
            }
            executeResponse(response, { mAddBnakRes.value = response.retData }, { errMsg.value = response.retMsg })
        }

    }

    /**
     * 提现
     */
    fun userWitraw(moneyE2: Int, payPassword: String) {
        val par = ConvertUtils.getRequest()
        par["moneyE2"] = (moneyE2 * 100).toString()
        par["payPassword"] = payPassword
        launch {
            val response = withContext(Dispatchers.IO) {
                mIncomeRepository.userWitraw(par)
            }
            executeResponse(response, { mWidt.value = response.retData }, { errMsg.value = response.retMsg })
        }

    }

    /**
     * 收益信息
     */
    fun useriNCOME(onlyList: String = "0"): MutableLiveData<IncomeParInfo> {
        val meMmpIncome = MutableLiveData<IncomeParInfo>()
        val par = ConvertUtils.getRequest()
        par["onlyList"] = onlyList
        launch {
            val response = withContext(Dispatchers.IO) {
                mIncomeRepository.useriNCOME(par)
            }
            executeResponse(response, {
                mIncome.value = response.retData
                meMmpIncome.value = response.retData
            }, { errMsg.value = response.retMsg })
        }
        return meMmpIncome
    }
}