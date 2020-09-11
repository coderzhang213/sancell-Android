package cn.sancell.xingqiu.goods.fragment.comment;

import android.content.Context;

import cn.sancell.xingqiu.base.RxUtils;
import cn.sancell.xingqiu.base.entity.BaseEntry;
import cn.sancell.xingqiu.base.entity.BaseObserver;
import cn.sancell.xingqiu.base.mvp.BasePresenter;
import cn.sancell.xingqiu.goods.entity.req.GoodsCommentReq;
import cn.sancell.xingqiu.homeclassify.bean.EvaluateListDataBean;
import cn.sancell.xingqiu.usermember.model.UserModel;

public class GoodsCommentPresenter extends BasePresenter<GoodsCommentView> {
    private Context mContext;

    public GoodsCommentPresenter(Context context) {
        mContext = context;
    }

    public void getCommentList(GoodsCommentReq req){

        UserModel.getInstance().getGoodsComments(req).compose(RxUtils.transformerWithLoading(getView()))
                .subscribe(new BaseObserver<EvaluateListDataBean>(mContext) {
                    @Override
                    protected void onSuccess(BaseEntry<EvaluateListDataBean> t) throws Exception {
                        if (getView() != null){
                            if (t.getRetCode() == 0){
                                getView().getCommentListSuccess(t.getRetData());
                            }else{
                                getView().getDataError(t.getRetMsg());
                            }
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (getView() != null){
                            getView().getDataError(e.toString());
                        }
                    }
                });
    }
}
