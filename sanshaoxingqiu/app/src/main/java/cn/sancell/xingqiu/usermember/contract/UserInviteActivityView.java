package cn.sancell.xingqiu.usermember.contract;

import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.usermember.bean.InviterBean;

/**
 * @author Alan_Xiong
 * @desc: 邀请人
 * @time 2019-10-22 11:32
 */
public interface UserInviteActivityView extends BaseView {

    void toast(String msg);

    void getInviteSuccess(InviterBean inviterBean);

    void bindInviteSuccess();

}
