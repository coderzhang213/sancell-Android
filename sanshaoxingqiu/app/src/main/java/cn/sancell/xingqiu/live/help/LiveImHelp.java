package cn.sancell.xingqiu.live.help;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.trello.rxlifecycle3.LifecycleTransformer;

import java.util.HashMap;

import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.ConvertUtils;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.goods.GoodsDetailActivity;
import cn.sancell.xingqiu.homecommunity.video.VideoView;
import cn.sancell.xingqiu.im.dialog.ApplyJoinTeamInputDialogFgm;
import cn.sancell.xingqiu.im.dialog.VideoRelDialogFgm;
import cn.sancell.xingqiu.im.entity.req.AddCartReq;
import cn.sancell.xingqiu.im.entity.res.VideoDetailRes;
import cn.sancell.xingqiu.im.entity.res.VideoRelationRes;
import cn.sancell.xingqiu.im.ui.listener.TeamApplyListener;
import cn.sancell.xingqiu.im.ui.red.call.ScClient;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.RetrofitUtil;
import cn.sancell.xingqiu.util.StringUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zj on 2019/11/27.
 */
public class LiveImHelp implements VideoView {
    private Activity mActivity;
    private String roomId;
    private String commType = "1";//1 是普通商品查看，2是主播商品查看
    private String mAnchorId;//主播Id


    public LiveImHelp(Activity mActivity, String roomId, String commType) {
        this.mActivity = mActivity;
        this.roomId = roomId;
        this.commType = commType;
        initView();
        initData();
    }

    /**
     * 设置主播ID
     *
     * @param mAnchorId
     */
    public void setmAnchorId(String mAnchorId) {
        this.mAnchorId = mAnchorId;
    }


    private void initView() {
    }

    public void click(String type) {

        getData(roomId, type);//被关联的商品(a)或者群组(b)
    }

    private void initData() {

    }


    /**
     * 获取关联商品
     */
    private void getData(String roomId, final String type) {
        if (TextUtils.isEmpty(roomId)) {
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        String reqTime = StringUtils.getCurrentTime();
        map.put("reqTime", reqTime);
        map.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
        map.put("hashToken", RSAUtils.encryptByPublic(map));
        map.put("batchId", roomId);
        map.put("type", type);
        RetrofitUtil.getInstance().initRetrofit().getLiveCommentList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<VideoRelationRes>(mActivity) {
                    @Override
                    protected void onSuccess(BaseEntry<VideoRelationRes> info) throws Exception {
                        if (type.equals("a")) {//被关联的商品(a)或者群组(b)
                            getVideoGoodsSuccess(info.getRetData());
                        } else {
                            getVideoTeamSuccess(info.getRetData());
                        }

                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                    }
                });
    }


    /**
     * 群组
     *
     * @param datas
     */
    @Override
    public void getVideoTeamSuccess(VideoRelationRes datas) {
        if (datas.dataList == null || datas.dataList.size() <= 0) {
            ToastHelper.showToast("本场直播没有推荐社群");
            return;
        }
        VideoRelDialogFgm dialogFgm = VideoRelDialogFgm.newInstance("3", datas.dataList);
        dialogFgm.setTv_title("推荐群组");
        dialogFgm.setDialogHeight(getDialogHeight());
        dialogFgm.setTeamClickListener(new VideoRelDialogFgm.OnTeamClickListener() {
            @Override
            public void onApplyJoin(String teamId) {
                dialogFgm.dismiss();
                ScClient.applyJoinWithVer(mActivity, teamId, PreferencesUtils.getString(Constants.Key.key_im_accid, "")
                        , new TeamApplyListener() {
                            @Override
                            public void showInputDialog() {
                                ApplyJoinTeamInputDialogFgm applyDialog = new ApplyJoinTeamInputDialogFgm();
                                applyDialog.setOnApplyListener(str ->
                                        ScClient.applyJoinTeam(mActivity, teamId, null,
                                                PreferencesUtils.getString(Constants.Key.key_im_accid, str), null));
                                applyDialog.show(((FragmentActivity) mActivity).getSupportFragmentManager(), "video");
                            }

                            @Override
                            public void onSuccess(Object o) {

                            }

                            @Override
                            public void onFailed(int i) {

                            }

                            @Override
                            public void onException(Throwable throwable) {

                            }
                        });


            }

            @Override
            public void onChat(String teamId) {
                ScClient.enterTeamChat(mActivity, teamId);
                dialogFgm.dismiss();
            }
        });
        dialogFgm.show(((FragmentActivity) mActivity).getSupportFragmentManager(), "team");
    }

    @Override
    public void getError(String error) {
        ToastHelper.showToast(mActivity, error);
    }

    @Override
    public void addCartSuccess() {
        SCApp.getInstance().showSystemCenterToast("已加入购物车");
    }

    @Override
    public void getVideoDetailSuccess(VideoDetailRes res) {

    }

    /**
     * 商品
     *
     * @param datas
     */
    @Override
    public void getVideoGoodsSuccess(VideoRelationRes datas) {
        if (datas.dataList == null || datas.dataList.size() <= 0) {
            ToastHelper.showToast("本场直播没有推荐商品");
            return;
        }
        VideoRelDialogFgm dialogFgm = VideoRelDialogFgm.newInstance(commType, datas.dataList);
        dialogFgm.setTv_title("推荐商品");
        dialogFgm.setDialogHeight(getDialogHeight());
        dialogFgm.setGoodsListener(new VideoRelDialogFgm.OnGoodsListener() {
            @Override
            public void addCart(String goodId) {
                //加车
//                AddCartReq req = new AddCartReq();
//                req.goodsId = goodId;
//                req.goodsNum = "1";
//                LiveAddCart(req);
                //商品详情
//                Intent intent = new Intent(mActivity, ProductInfoActivity.class);
//                intent.putExtra(Constants.Key.KEY_1, goodId);
//                mActivity.startActivity(intent);
                GoodsDetailActivity.start(mActivity, Integer.parseInt(goodId), roomId);
                dialogFgm.dismiss();
            }

            @Override
            public void goodDetail(String goodsId) {
                //商品详情
//                Intent intent = new Intent(mActivity, ProductInfoActivity.class);
//                intent.putExtra(Constants.Key.KEY_1, goodsId);
//                mActivity.startActivity(intent);
                GoodsDetailActivity.start(mActivity, Integer.parseInt(goodsId), roomId);

                dialogFgm.dismiss();

            }
        });
        dialogFgm.show(((FragmentActivity) mActivity).getSupportFragmentManager(), "goods");
    }

    public int getDialogHeight() {
        return (int) (ScreenUtil.screenHeight * 0.6);
    }

    public void LiveAddCart(AddCartReq req) {
        HashMap<String, String> map = ConvertUtils.convertToMap(req);
        RetrofitUtil.getInstance().initRetrofit().addCart(map).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver(mActivity) {
                    @Override
                    protected void onSuccess(BaseEntry info) throws Exception {
                        if (getView() != null) {
                            if (info.getRetCode() == 0) {
                                getView().addCartSuccess();
                            } else {
                                getView().getError(info.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null) {
                            getView().getError(e.toString());
                        }
                    }
                });
    }

    @Override
    public void showLoading(boolean show) {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return null;
    }

    public VideoView getView() {
        return this;
    }

}
