package cn.sancell.xingqiu.im.ui.red.empty;

import android.app.Activity;
import android.content.Intent;

import com.jrmf360.normallib.rp.bean.GrabRpBean;
import com.jrmf360.normallib.rp.utils.callback.GrabRpCallBack;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.IntentKey;
import cn.sancell.xingqiu.im.entity.req.RpGrabReq;
import cn.sancell.xingqiu.im.entity.res.CheckRpRes;
import cn.sancell.xingqiu.im.entity.res.RpDetailRes;
import cn.sancell.xingqiu.im.entity.res.RpGrabRes;

/**
  * @author Alan_Xiong
  *
  * @desc: 空页面实现网络请求与回调
  * @time 2019-11-18 16:26
  */
public class RedEmptyActivity extends BaseMVPActivity<RedEmptyPresenter> implements RedEmptyView {

    private String rpId;

    public static void start(Activity activity,String rpId){
        Intent intent = new Intent(activity,RedEmptyActivity.class);
        intent.putExtra(IntentKey.rp_id,rpId);
        activity.startActivity(intent);
    }

    @Override
    protected RedEmptyPresenter createPresenter() {
        return new RedEmptyPresenter(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_empty;
    }

    @Override
    protected void initial() {
        rpId = getIntent().getStringExtra(IntentKey.rp_id);
        checkRp();
    }

    @Override
    protected BaseView getView() {
        return this;
    }


    public static void setGrabRpCallBack(GrabRpCallBack callBack){
    }

    public void checkRp(){
        RpGrabReq req = new RpGrabReq();
        req.redId = rpId;
        mPresenter.checkRp(req);
    }


    @Override
    public void checkRpSuccess(CheckRpRes res) {
        SCApp.getInstance().showSystemCenterToast(res.valid+"");
    }

    @Override
    public void checkRpError(String str) {
       finish();
    }

    @Override
    public void getRpDetailSuccess(RpDetailRes res) {

    }

    @Override
    public void getRpDetailError(String str) {

    }

    @Override
    public void grabRpSuccess(RpGrabRes res) {

    }

    @Override
    public void grabRpError(String str) {

    }
}
