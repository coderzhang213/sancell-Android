package cn.sancell.xingqiu.util;

import android.content.Context;

import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.im.entity.res.ImAccountRes;
import cn.sancell.xingqiu.im.sys.ImCache;
import cn.sancell.xingqiu.live.user.LiveCache;
import cn.sancell.xingqiu.login.bean.UserLoginDataBean;

/**
 * @author Alan_Xiong
 * @desc: sp 数据操作类
 * @time 2019-11-20 21:31
 */
public class SPManager {


    private static final class SpHolder {
        private static final SPManager INSTANCE = new SPManager();
    }

    public static SPManager getInstance() {
        return SpHolder.INSTANCE;
    }

    public void putLoginAccount(UserLoginDataBean bean) {
        PreferencesUtils.put(Constants.Key.KEY_USERINFO, bean.getUser());
        PreferencesUtils.put(Constants.Key.KEY_SKEY, bean.getSkey());
        PreferencesUtils.put(Constants.Key.key_im_accid, bean.yunxin_accid);
        PreferencesUtils.put(Constants.Key.key_im_token, bean.yunxin_token);
        PreferencesUtils.put(Constants.Key.key_im_user_name, bean.yunxin_name);
        //初始化im相关账户
        ImCache.setAccount(bean.yunxin_name);
        LiveCache.setAccount(bean.yunxin_name);
    }

    /**
     * 保存登陆的账号
     *
     * @param res
     */
    public void putImAccount(ImAccountRes res) {
        PreferencesUtils.put(Constants.Key.key_im_accid, res.yunxin_accid);
        PreferencesUtils.put(Constants.Key.key_im_token, res.yunxin_token);
        PreferencesUtils.put(Constants.Key.key_im_user_name, res.yunxin_name);
        ImCache.setAccount(res.yunxin_name);
        LiveCache.setAccount(res.yunxin_name);
    }


    /**
     * 移除登陆的账号
     *
     * @param context
     */
    public void removeAccount(Context context) {
        PreferencesUtils.remove(context, Constants.Key.KEY_SKEY);
        PreferencesUtils.remove(context, Constants.Key.KEY_USERINFO);
        PreferencesUtils.remove(context, Constants.Key.key_im_accid);
        PreferencesUtils.remove(context, Constants.Key.key_im_token);
        PreferencesUtils.remove(context, Constants.Key.key_im_user_name);
    }
}
