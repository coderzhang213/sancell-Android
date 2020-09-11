package cn.sancell.xingqiu.homeuser.viewModel

import androidx.lifecycle.MutableLiveData
import cn.sancell.xingqiu.base.ConvertUtils
import cn.sancell.xingqiu.bean.VoucherCenterRes
import cn.sancell.xingqiu.bean.VoucherInfo
import cn.sancell.xingqiu.homeuser.repository.VoucherRepository
import handbank.hbwallet.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VoucherViewModel :BaseViewModel(){

    private val mRepository by lazy { VoucherRepository() }

    var mVoucherList = MutableLiveData<VoucherCenterRes>()

    fun getVoucherList(type:Int){
        val params = ConvertUtils.getRequest()
        params["type"] = type.toString()
        launch{
            val response = withContext(Dispatchers.IO){
                mRepository.getVoucherList(params)
            }
            executeResponse(response,{mVoucherList.value = response.retData},{errMsg.value = response.retMsg})
        }
    }
}