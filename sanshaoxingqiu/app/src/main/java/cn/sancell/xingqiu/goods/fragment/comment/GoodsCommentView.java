package cn.sancell.xingqiu.goods.fragment.comment;

import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.homeclassify.bean.EvaluateListDataBean;

public interface GoodsCommentView extends BaseView {

    void getCommentListSuccess(EvaluateListDataBean res);

    void getDataError(String error);
}
