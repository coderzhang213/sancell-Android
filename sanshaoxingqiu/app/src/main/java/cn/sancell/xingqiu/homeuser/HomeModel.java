package cn.sancell.xingqiu.homeuser;

import java.util.HashMap;

import cn.sancell.xingqiu.base.ConvertUtils;
import cn.sancell.xingqiu.base.base.BaseReq;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.homeuser.bean.res.ActivityConfigRes;
import cn.sancell.xingqiu.util.RetrofitUtil;
import io.reactivex.Observable;

/**
  * @author Alan_Xiong
  *
  * @desc: 首页相关model
  * @time 2019-12-05 16:57
  */
public class HomeModel {

    private static final class Holder{
        private static final HomeModel instance = new HomeModel();
    }

    public static HomeModel getInstance() {
        return Holder.instance;
    }


    public Observable<BaseEntry<ActivityConfigRes>> getActivityConfig(BaseReq req){
        HashMap<String,String> map = ConvertUtils.convertToMap(req);
        return RetrofitUtil.getInstance().initRetrofit().getActivityConfig(map);
    }


}
