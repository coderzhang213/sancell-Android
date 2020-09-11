package cn.sancell.xingqiu.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.TakePhotoOptions;
import org.w3c.dom.Text;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import cn.sancell.xingqiu.BuildConfig;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.constant.UrlConstants;
import cn.sancell.xingqiu.goods.GoodsDetailActivity;
import cn.sancell.xingqiu.homeclassify.ProductInfoActivity;
import cn.sancell.xingqiu.homepage.UrlInfoActivity;
import cn.sancell.xingqiu.homepage.bean.NavigationInfoBean;
import cn.sancell.xingqiu.homeuser.ModifyPayPwdCheckOldPwdActivity;
import cn.sancell.xingqiu.homeuser.ModifyPayPwdCheckPhoneActivity;
import cn.sancell.xingqiu.homeuser.adapter.OrderCancelReasonListAdapter;
import cn.sancell.xingqiu.homeuser.bean.OrderCancelReasonListBean;
import cn.sancell.xingqiu.im.entity.res.RpDetailRes;
import cn.sancell.xingqiu.im.ui.red.call.RedClickListener;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.picker.DatePicker;
import cn.sancell.xingqiu.tools.GlideEngine;
import cn.sancell.xingqiu.usermember.MemberVipGiftBuyActivity;

/**
 * Created by camelliae on 2017/2/7.
 */
public class DialogUtil {
    /*public static void showAppUpdateDialog(final Activity context, final AppUpdateInfo info, final AppUpdateInfoForInstall infoForInstall, DialogInterface.OnKeyListener keylistener, final ClickCancelAction clickSureAction) {
        View view = context.getLayoutInflater().inflate(R.layout.update_dialog,
                null);
        final Dialog dialog = new Dialog(context, R.style.bdp_update_dialog_style);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ScreenUtils.getScreenWidth(context) * 8 / 10,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.setOnKeyListener(keylistener);
        TextView txt_title = (TextView) dialog.getWindow()
                .findViewById(R.id.txt_title);
        TextView txt_main_tip = (TextView) dialog.getWindow()
                .findViewById(R.id.txt_main_tip);
        final TextView txt_minor_tip = (TextView) dialog.getWindow()
                .findViewById(R.id.txt_minor_tip);
        final Button btn_action_1 = (Button) dialog.getWindow()
                .findViewById(R.id.btn_action_1);
        TextView txt_action_2 = (TextView) dialog.getWindow()
                .findViewById(R.id.txt_action_2);
        final ProgressBar progressBar = (ProgressBar) dialog.getWindow()
                .findViewById(R.id.update_progress);
        progressBar.setMax(100);


        if (infoForInstall != null && !TextUtils.isEmpty(infoForInstall.getInstallPath())) {
            txt_title.setText("发现新版本安装包");
            txt_main_tip.setText(AppUtils.getVersionName(context) + "-->" + infoForInstall.getAppVersionName());
            txt_minor_tip.setText(Html.fromHtml(infoForInstall.getAppChangeLog()));
            btn_action_1.setText("立即安装");
            txt_action_2.setText("取消安装");
            btn_action_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    BDAutoUpdateSDK.cpUpdateInstall(context, infoForInstall.getInstallPath());
                }
            });
            txt_action_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                }
            });

            dialog.setCanceledOnTouchOutside(false);// 设置点击dialog外面不消失
            dialog.show();

        } else if (info != null) {
            txt_title.setText("发现新版本");
            txt_main_tip.setText(AppUtils.getVersionName(context) + "-->" + info.getAppVersionName());
            txt_minor_tip.setText("更新内容" + "\n" + Html.fromHtml(info.getAppChangeLog()));
            btn_action_1.setText("立即升级");
            txt_action_2.setText("取消升级");
            btn_action_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_action_1.setClickable(false);
                    txt_minor_tip.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    BDAutoUpdateSDK.cpUpdateDownload(context, info, new CPUpdateDownloadCallback() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onPercent(int percent, long l, long l1) {
                            progressBar.setProgress(percent);
                        }

                        @Override
                        public void onDownloadComplete(String apkPath) {
                            progressBar.setVisibility(View.GONE);
                            txt_minor_tip.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                            BDAutoUpdateSDK.cpUpdateInstall(context, apkPath);
                        }

                        @Override
                        public void onFail(Throwable throwable, String content) {
                            btn_action_1.setClickable(true);
                            SCApp.getInstance().showSystemCenterToast(content);
                            progressBar.setVisibility(View.GONE);
                            txt_minor_tip.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onStop() {

                        }
                    });
                }
            });
            txt_action_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                }
            });
            dialog.setCanceledOnTouchOutside(false);// 设置点击dialog外面不消失
            dialog.show();
        }
    }*/

    public interface ClickCancelAction {
        void cancelAction();
    }

    public interface GetDateAction {
        void sureAction(String year, String month, String day);
    }

    public static void onYearMonthDayPicker(Activity context, final GetDateAction getDateAction) {
        Calendar calendar = Calendar.getInstance();
        final DatePicker picker = new DatePicker(context);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ScreenUtils.dip2px(context, 10));
        picker.setRangeEnd(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        picker.setRangeStart(1940, 1, 1);
        picker.setSelectedItem(2000, 6, 14);
        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener((DatePicker.OnYearMonthDayPickListener) (year, month, day) -> {
            getDateAction.sureAction(year, month, day);
            picker.dismiss();
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }

    /**
     * 遮罩式广告弹窗
     *
     * @param context
     * @param navigationInfo
     */
    public static void showNavigationInfoDialog(Activity context, NavigationInfoBean navigationInfo) {
        int w = ScreenUtils.getScreenWidth(context) * 275 / 375;
        int h = w;
        if (navigationInfo.getPicObjInfo() != null && navigationInfo.getPicObjInfo().getPicWidth() > 0 && navigationInfo.getPicObjInfo().getPicHeight() > 0) {
            h = w * navigationInfo.getPicObjInfo().getPicHeight() / navigationInfo.getPicObjInfo().getPicWidth();
        }
        View view = context.getLayoutInflater().inflate(R.layout.dialog_navigationinfo,
                null);
        final Dialog dialog_message = new Dialog(context, R.style.transparentFrameWindowStyle);
        dialog_message.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_message.setContentView(view, new ViewGroup.LayoutParams(w,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        // 设置点击外围解散
        dialog_message.setCanceledOnTouchOutside(true);
        SimpleDraweeView sdv_dialog_pic = view.findViewById(R.id.sdv_dialog_pic);
        sdv_dialog_pic.setLayoutParams(new LinearLayout.LayoutParams(w, h));
        sdv_dialog_pic.setImageURI(Uri.parse(navigationInfo.getPicObjInfo().getCoverPic()));
        ImageView iv_close = view.findViewById(R.id.iv_close);

        iv_close.setOnClickListener(view12 -> dialog_message.dismiss());
        sdv_dialog_pic.setOnClickListener(view1 -> {
            dialog_message.dismiss();
            switch (navigationInfo.getDataType()) {
                case 1:
                    switch (navigationInfo.getObjType()) {
                        case 1: //商品
                            GoodsDetailActivity.start(context, navigationInfo.getObjId());
                            break;
                    }
                    break;
                case 2:  //外链
                    Intent intent33 = new Intent();
                    intent33.setAction(Intent.ACTION_VIEW);
                    intent33.setData(Uri.parse(navigationInfo.getWebUrl()));
                    if (intent33.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(Intent.createChooser(intent33, "请选择浏览器"));
                    } else {
                        SCApp.getInstance().showSystemCenterToast("请先下载浏览器");
                    }
                    break;
            }
        });
        dialog_message.show();
    }


    public static void showOperateDialog(Activity context, String title, String desc, String cancel, String sure, final ClickSureAction clickSureAction) {
        View view = context.getLayoutInflater().inflate(R.layout.dialog_operate_tip,
                null);
        final Dialog dialog_message = new Dialog(context, R.style.transparentFrameWindowStyle);
        dialog_message.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_message.setContentView(view, new ViewGroup.LayoutParams(ScreenUtils.getScreenWidth(context) * 295 / 375,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // 设置点击外围解散
        dialog_message.setCanceledOnTouchOutside(true);
        TextView tv_titel = view.findViewById(R.id.tv_title);
        TextView tv_desc = view.findViewById(R.id.tv_desc);
        TextView tv_sure = view.findViewById(R.id.tv_sure);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_titel.setText(title);
        if (StringUtils.isTextEmpty(desc)) {
            tv_desc.setVisibility(View.GONE);
        } else {
            tv_desc.setVisibility(View.VISIBLE);
            tv_desc.setText(desc);
        }
        tv_cancel.setText(cancel);
        tv_sure.setText(sure);

        tv_cancel.setOnClickListener(view12 -> dialog_message.dismiss());
        tv_sure.setOnClickListener(view1 -> {
            dialog_message.dismiss();
            clickSureAction.sureAction(0);
        });
        dialog_message.show();
    }

    public static void showOperateDialog(Activity context, String title, String desc, String cancel, String sure, final ClickCancelAction clickCancelAction) {
        View view = context.getLayoutInflater().inflate(R.layout.dialog_operate_tip,
                null);
        final Dialog dialog_message = new Dialog(context, R.style.transparentFrameWindowStyle);
        dialog_message.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_message.setContentView(view, new ViewGroup.LayoutParams(ScreenUtils.getScreenWidth(context) * 295 / 375,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // 设置点击外围解散
        dialog_message.setCanceledOnTouchOutside(true);
        TextView tv_titel = view.findViewById(R.id.tv_title);
        TextView tv_desc = view.findViewById(R.id.tv_desc);
        TextView tv_sure = view.findViewById(R.id.tv_sure);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_titel.setText(title);
        if (StringUtils.isTextEmpty(desc)) {
            tv_desc.setVisibility(View.GONE);
        } else {
            tv_desc.setVisibility(View.VISIBLE);
            tv_desc.setText(desc);
        }
        tv_cancel.setText(cancel);
        tv_sure.setText(sure);

        tv_cancel.setOnClickListener(view12 -> {
            dialog_message.dismiss();
            clickCancelAction.cancelAction();
        });
        tv_sure.setOnClickListener(view1 -> dialog_message.dismiss());
        dialog_message.show();
    }

    public static void showOperateDialog(Activity context, String title, String desc, String cancel, String sure, int postion, final ClickSureAction clickSureAction) {
        View view = context.getLayoutInflater().inflate(R.layout.dialog_operate_tip,
                null);
        final Dialog dialog_message = new Dialog(context, R.style.transparentFrameWindowStyle);
        dialog_message.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_message.setContentView(view, new ViewGroup.LayoutParams(ScreenUtils.getScreenWidth(context) * 295 / 375,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // 设置点击外围解散
        dialog_message.setCanceledOnTouchOutside(true);
        TextView tv_titel = view.findViewById(R.id.tv_title);
        TextView tv_desc = view.findViewById(R.id.tv_desc);
        TextView tv_sure = view.findViewById(R.id.tv_sure);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_titel.setText(title);
        if (StringUtils.isTextEmpty(desc)) {
            tv_desc.setVisibility(View.GONE);
        } else {
            tv_desc.setVisibility(View.VISIBLE);
            tv_desc.setText(desc);
        }
        tv_cancel.setText(cancel);
        tv_sure.setText(sure);

        tv_cancel.setOnClickListener(view12 -> dialog_message.dismiss());
        tv_sure.setOnClickListener(view1 -> {
            dialog_message.dismiss();
            clickSureAction.sureAction(postion);
        });
        dialog_message.show();
    }

    public interface ClickSureAction {
        void sureAction(int postion);
    }


    public interface ClickCommitAction {
        void commitAction(String reason);

        void commitAction(String select_reason, int postion);
    }

    /**
     * 取消订单弹框
     */
    private static String select_reason = "";

    public static void showCancelOrder(Activity context, List<OrderCancelReasonListBean.OrderCancelReasonBean> data_reason, ClickCommitAction clickCommitAction) {

        View view = context.getLayoutInflater().inflate(R.layout.dialog_order_cancel,
                null);
        Dialog dialog_cancel = new Dialog(context, R.style.transparentFrameWindowStyle);
        dialog_cancel.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ImageView iv_dialog_close = view.findViewById(R.id.iv_dialog_close);
        RecyclerView rcv_cancel_reason = view.findViewById(R.id.rcv_cancel_reason);
        TextView btn_commit = view.findViewById(R.id.btn_commit);
        btn_commit.setClickable(false);
        rcv_cancel_reason.setLayoutManager(new LinearLayoutManager(context));
        OrderCancelReasonListAdapter orderCancelReasonListAdapter = new OrderCancelReasonListAdapter(data_reason);
        rcv_cancel_reason.setAdapter(orderCancelReasonListAdapter);
        orderCancelReasonListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < data_reason.size(); i++) {
                    if (i == position) {
                        data_reason.get(i).setSelect(true);
                    } else {
                        data_reason.get(i).setSelect(false);
                    }
                }
                orderCancelReasonListAdapter.notifyDataSetChanged();
                btn_commit.setEnabled(true);
                btn_commit.setClickable(true);
                select_reason = data_reason.get(position).getReasonId() + "";
            }
        });

        iv_dialog_close.setOnClickListener(view12 -> dialog_cancel.dismiss());
        btn_commit.setOnClickListener(view1 -> {
            dialog_cancel.dismiss();
            clickCommitAction.commitAction(select_reason);
        });
        Window window = dialog_cancel.getWindow();
        /**
         * 位于底部
         */
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        /**
         * 设置弹出动画
         */
        window.setWindowAnimations(R.style.ani_bottom);
        // 设置点击外围解散
        dialog_cancel.setCanceledOnTouchOutside(true);
        dialog_cancel.show();
    }

    /**
     * 列表中取消订单弹框
     */
    static String select_reason1 = "";

    public static void showCancelOrder(Activity context, List<OrderCancelReasonListBean.OrderCancelReasonBean> data_reason, int pos, ClickCommitAction clickCommitAction) {
        View view = context.getLayoutInflater().inflate(R.layout.dialog_order_cancel,
                null);
        Dialog dialog_cancel = new Dialog(context, R.style.transparentFrameWindowStyle);
        dialog_cancel.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ImageView iv_dialog_close = view.findViewById(R.id.iv_dialog_close);
        RecyclerView rcv_cancel_reason = view.findViewById(R.id.rcv_cancel_reason);
        TextView btn_commit = view.findViewById(R.id.btn_commit);
        btn_commit.setClickable(false);
        rcv_cancel_reason.setLayoutManager(new LinearLayoutManager(context));
        OrderCancelReasonListAdapter orderCancelReasonListAdapter = new OrderCancelReasonListAdapter(data_reason);
        rcv_cancel_reason.setAdapter(orderCancelReasonListAdapter);
        orderCancelReasonListAdapter.setOnItemChildClickListener((adapter, view13, position) -> {
            for (int i = 0; i < data_reason.size(); i++) {
                if (i == position) {
                    data_reason.get(i).setSelect(true);
                } else {
                    data_reason.get(i).setSelect(false);
                }
            }
            orderCancelReasonListAdapter.notifyDataSetChanged();
            btn_commit.setEnabled(true);
            btn_commit.setClickable(true);
            select_reason1 = data_reason.get(position).getReasonId() + "";
        });

        iv_dialog_close.setOnClickListener(view12 -> dialog_cancel.dismiss());
        btn_commit.setOnClickListener(view1 -> {
            dialog_cancel.dismiss();
            clickCommitAction.commitAction(select_reason1, pos);
        });
        Window window = dialog_cancel.getWindow();
        /**
         * 位于底部
         */
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        /**
         * 设置弹出动画
         */
        window.setWindowAnimations(R.style.ani_bottom);
        // 设置点击外围解散
        dialog_cancel.setCanceledOnTouchOutside(true);
        dialog_cancel.show();
    }


    /**
     * 修改昵称对话框
     *
     * @param context
     * @param clickCommitAction
     */
    public static void showModifyNickName(Activity context, ClickCommitAction clickCommitAction, String nickname) {
        View view = context.getLayoutInflater().inflate(R.layout.dialog_modify_nickname,
                null);
        Dialog dialog_cancel = new Dialog(context, R.style.transparentFrameWindowStyle);
        dialog_cancel.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ImageView iv_dialog_close = view.findViewById(R.id.iv_dialog_close);
        TextView btn_commit = view.findViewById(R.id.btn_commit);
        if (!StringUtils.isTextEmpty(nickname) && nickname.length() >= 2) {
            btn_commit.setEnabled(true);
            btn_commit.setClickable(true);
        } else {
            btn_commit.setEnabled(false);
            btn_commit.setClickable(false);
        }
        EditText ed_nickname = view.findViewById(R.id.ed_nickname);
        /*final int maxLen = 32;
        InputFilter filter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {
                int dindex = 0;
                int count = 0;

                while (count <= maxLen && dindex < dest.length()) {
                    char c = dest.charAt(dindex++);
                    if (c < 128) {
                        count = count + 1;
                    } else {
                        count = count + 2;
                    }
                }

                if (count > maxLen) {
                    return dest.subSequence(0, dindex - 1);
                }

                int sindex = 0;
                while (count <= maxLen && sindex < src.length()) {
                    char c = src.charAt(sindex++);
                    if (c < 128) {
                        count = count + 1;
                    } else {
                        count = count + 2;
                    }
                }

                if (count > maxLen) {
                    sindex--;
                }

                return src.subSequence(0, sindex);
            }
        };
        ed_nickname.setFilters(new InputFilter[]{filter});*/
        ed_nickname.setText(nickname);
        ed_nickname.setSelection(ed_nickname.getText().length());
        ed_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (StringUtils.judgeTextLength(editable.toString()) >= 2 && StringUtils.judgeTextLength(editable.toString()) <= 32) {
                    btn_commit.setEnabled(true);
                    btn_commit.setClickable(true);
                } else {
                    btn_commit.setEnabled(false);
                    btn_commit.setClickable(false);
                }
            }
        });
        iv_dialog_close.setOnClickListener(view1 -> dialog_cancel.dismiss());
        btn_commit.setOnClickListener(view12 -> {
            dialog_cancel.dismiss();

            clickCommitAction.commitAction(ed_nickname.getText().toString());
        });
        Window window = dialog_cancel.getWindow();
        /**
         * 位于底部
         */
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        /**
         * 设置弹出动画
         */
        window.setWindowAnimations(R.style.ani_bottom);
        // 设置点击外围解散
        dialog_cancel.setCanceledOnTouchOutside(true);
        dialog_cancel.show();
    }


    /**
     * 修改性别对话框
     *
     * @param context
     * @param clickCommitAction
     */
    static int select_sex;

    public static void showModifySex(Activity context, ClickCommitAction clickCommitAction, int sex) {
        View view = context.getLayoutInflater().inflate(R.layout.dialog_modify_sex,
                null);
        Dialog dialog_cancel = new Dialog(context, R.style.transparentFrameWindowStyle);
        dialog_cancel.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        LinearLayout ll_female = view.findViewById(R.id.ll_female);
        LinearLayout ll_male = view.findViewById(R.id.ll_male);
        ImageView iv_dialog_close = view.findViewById(R.id.iv_dialog_close);
        TextView btn_commit = view.findViewById(R.id.btn_commit);
        ImageView iv_female = view.findViewById(R.id.iv_female);
        ImageView iv_male = view.findViewById(R.id.iv_male);
        btn_commit.setEnabled(false);
        btn_commit.setClickable(false);
        if (sex == 2) {  //女
            select_sex = 2;
            iv_female.setImageResource(R.mipmap.icon_car_select_yes);
            iv_male.setImageResource(R.mipmap.icon_car_select_no);
            btn_commit.setEnabled(true);
            btn_commit.setClickable(true);
        } else if (sex == 1) {  //男
            select_sex = 1;
            iv_male.setImageResource(R.mipmap.icon_car_select_yes);
            iv_female.setImageResource(R.mipmap.icon_car_select_no);
            btn_commit.setEnabled(true);
            btn_commit.setClickable(true);
        }
        ll_female.setOnClickListener(view14 -> {
            btn_commit.setEnabled(true);
            btn_commit.setClickable(true);
            iv_female.setImageResource(R.mipmap.icon_car_select_yes);
            iv_male.setImageResource(R.mipmap.icon_car_select_no);
            select_sex = 2;
        });
        ll_male.setOnClickListener(view13 -> {
            btn_commit.setEnabled(true);
            btn_commit.setClickable(true);
            iv_male.setImageResource(R.mipmap.icon_car_select_yes);
            iv_female.setImageResource(R.mipmap.icon_car_select_no);
            select_sex = 1;
        });
        iv_dialog_close.setOnClickListener(view12 -> dialog_cancel.dismiss());
        btn_commit.setOnClickListener(view1 -> {
            dialog_cancel.dismiss();
            clickCommitAction.commitAction(select_sex + "");
        });
        Window window = dialog_cancel.getWindow();
        /**
         * 位于底部
         */
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        /**
         * 设置弹出动画
         */
        window.setWindowAnimations(R.style.ani_bottom);
        // 设置点击外围解散
        dialog_cancel.setCanceledOnTouchOutside(true);
        dialog_cancel.show();
    }


//    public static void showModifyPhoto(Activity context, TakePhoto takePhoto) {
//        View view = context.getLayoutInflater().inflate(R.layout.dialog_tow_select_operate,
//                null);
//        Dialog dialog_cancel = new Dialog(context, R.style.transparentFrameWindowStyle);
//        dialog_cancel.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
//        TextView tv_operate1 = view.findViewById(R.id.tv_operate1);
//        TextView tv_operate2 = view.findViewById(R.id.tv_operate2);
//        tv_operate1.setText(R.string.take);
//        tv_operate2.setText(R.string.gallery);
//        tv_operate1.setOnClickListener(view13 -> {
//            dialog_cancel.dismiss();
//            File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
//            if (!file.getParentFile().exists()) {
//                file.getParentFile().mkdirs();
//            }
//            Uri imageUri = Uri.fromFile(file);
//            configCompress(takePhoto);
//            configTakePhotoOption(takePhoto);
//            takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
//        });
//        tv_operate2.setOnClickListener(view12 -> {
//            dialog_cancel.dismiss();
//            File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
//            if (!file.getParentFile().exists()) {
//                file.getParentFile().mkdirs();
//            }
//            Uri imageUri = Uri.fromFile(file);
//            configCompress(takePhoto);
//            configTakePhotoOption(takePhoto);
//            takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
//        });
//        tv_cancel.setOnClickListener(view1 -> dialog_cancel.dismiss());
//        Window window = dialog_cancel.getWindow();
//        /**
//         * 位于底部
//         */
//        window.setGravity(Gravity.BOTTOM);
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        window.setAttributes(params);
//        /**
//         * 设置弹出动画
//         */
//        window.setWindowAnimations(R.style.ani_bottom);
//        // 设置点击外围解散
//        dialog_cancel.setCanceledOnTouchOutside(true);
//        dialog_cancel.show();
//    }

    public static void showModifyPhoto(Activity mActivity) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.dialog_tow_select_operate,
                null);
        Dialog dialog_cancel = new Dialog(mActivity, R.style.transparentFrameWindowStyle);
        dialog_cancel.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        TextView tv_operate1 = view.findViewById(R.id.tv_operate1);
        TextView tv_operate2 = view.findViewById(R.id.tv_operate2);
        tv_operate1.setText(R.string.take);
        tv_operate2.setText(R.string.gallery);
        tv_operate1.setOnClickListener(view13 -> {
            dialog_cancel.dismiss();
            File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            PictureSelector.create(mActivity).openCamera(PictureMimeType.ofImage())
                    .isWeChatStyle(true)
                    .compress(true)
                    .loadImageEngine(GlideEngine.createGlideEngine())
                    .forResult(PictureConfig.REQUEST_CAMERA);


        });
        tv_operate2.setOnClickListener(view12 -> {
            dialog_cancel.dismiss();
            File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            PictureSelector.create(mActivity).openGallery(PictureMimeType.ofImage())
                    .isWeChatStyle(true)
                    .selectionMode(PictureConfig.SINGLE)
                    .compress(true)
                    .compressSavePath(file.getPath())
                    .loadImageEngine(GlideEngine.createGlideEngine())
                    .forResult(PictureConfig.CHOOSE_REQUEST);

        });
        tv_cancel.setOnClickListener(view1 -> dialog_cancel.dismiss());
        Window window = dialog_cancel.getWindow();
        /**
         * 位于底部
         */
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        /**
         * 设置弹出动画
         */
        window.setWindowAnimations(R.style.ani_bottom);
        // 设置点击外围解散
        dialog_cancel.setCanceledOnTouchOutside(true);
        dialog_cancel.show();
    }


    private static void configCompress(TakePhoto takePhoto) {
        int maxSize = 500 * 1024;
        int width = 200;
        int height = 200;
        CompressConfig config;
        config = new CompressConfig.Builder().setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(true)
                .create();
        takePhoto.onEnableCompress(config, true);


    }

    private static CropOptions getCropOptions() {
        int height = 400;
        int width = 400;
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setAspectX(width).setAspectY(height);
        builder.setWithOwnCrop(false);
        return builder.create();
    }

    private static void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);
        builder.setCorrectImage(true);
        takePhoto.setTakePhotoOptions(builder.create());
    }

    /**
     * 拨打电话联系我们对话框
     *
     * @param context
     */
    @SuppressLint("MissingPermission")
    public static void showCallPhone(Activity context, String goodsId, String orderId) {
        View view = context.getLayoutInflater().inflate(R.layout.dialog_call_phone,
                null);
        Dialog dialog_cancel = new Dialog(context, R.style.transparentFrameWindowStyle);
        dialog_cancel.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        TextView tv_phone1 = view.findViewById(R.id.tv_phone1);
        TextView tv_phone2 = view.findViewById(R.id.tv_phone2);
        TextView tv_phone3 = view.findViewById(R.id.tv_phone3);
        TextView tv_phone5 = view.findViewById(R.id.tv_phone5);
        TextView tv_phone6 = view.findViewById(R.id.tv_phone6);
        tv_phone5.setOnClickListener(view13 -> {
            dialog_cancel.dismiss();
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + tv_phone5.getText().toString().replaceAll("-", ""));
            intent.setData(data);
            context.startActivity(intent);

        });
        tv_phone6.setOnClickListener(view13 -> {
            dialog_cancel.dismiss();
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + tv_phone6.getText().toString().replaceAll("-", ""));
            intent.setData(data);
            context.startActivity(intent);

        });
        tv_phone1.setOnClickListener(view13 -> {
            dialog_cancel.dismiss();
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + tv_phone1.getText().toString().replaceAll("-", ""));
            intent.setData(data);
            context.startActivity(intent);

        });
        tv_phone2.setOnClickListener(view12 -> {
            dialog_cancel.dismiss();
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + tv_phone2.getText().toString().replaceAll("-", ""));
            intent.setData(data);
            context.startActivity(intent);
        });
        tv_phone3.setOnClickListener(view14 -> {
            String url = BuildConfig.H5_API + UrlConstants.SERVER_API + "goodsId=" + goodsId + "&orderId=" + orderId + "&skey=" + PreferencesUtils.getString(Constants.Key.KEY_SKEY, "");

            Intent intent2 = new Intent(context, UrlInfoActivity.class);
            intent2.putExtra(Constants.Key.KEY_1, url);
            intent2.putExtra(Constants.Key.KEY_2, "");
            context.startActivity(intent2);
        });
        tv_cancel.setOnClickListener(view1 -> dialog_cancel.dismiss());
        Window window = dialog_cancel.getWindow();
        /**
         * 位于底部
         */
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        /**
         * 设置弹出动画
         */
        window.setWindowAnimations(R.style.ani_bottom);
        // 设置点击外围解散
        dialog_cancel.setCanceledOnTouchOutside(true);
        dialog_cancel.show();
    }

    /**
     * 选择修改和忘记支付密码对话框
     *
     * @param context
     */

    public static void showModifyPayPwd(Activity context) {
        View view = context.getLayoutInflater().inflate(R.layout.dialog_tow_select_operate,
                null);
        Dialog dialog_cancel = new Dialog(context, R.style.transparentFrameWindowStyle);
        dialog_cancel.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        TextView tv_operate1 = view.findViewById(R.id.tv_operate1);
        TextView tv_operate2 = view.findViewById(R.id.tv_operate2);
        tv_operate1.setText(R.string.modify_pay_pwd);
        tv_operate2.setText(R.string.forget_pay_pwd);
        tv_operate1.setOnClickListener(view12 -> {
            dialog_cancel.dismiss();
            Intent intent = new Intent(context, ModifyPayPwdCheckOldPwdActivity.class);
            context.startActivity(intent);
        });
        tv_operate2.setOnClickListener(view1 -> {
            dialog_cancel.dismiss();
            Intent intent = new Intent(context, ModifyPayPwdCheckPhoneActivity.class);
            context.startActivity(intent);
        });
        tv_cancel.setOnClickListener(view13 -> dialog_cancel.dismiss());
        Window window = dialog_cancel.getWindow();
        /**
         * 位于底部
         */
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        /**
         * 设置弹出动画
         */
        window.setWindowAnimations(R.style.ani_bottom);
        // 设置点击外围解散
        dialog_cancel.setCanceledOnTouchOutside(true);
        dialog_cancel.show();
    }

    /**
     * 购物送红包相关红包规则信息对话框
     */
    public static void showRedPacket(Activity context) {
        View view_red_packet = context.getLayoutInflater().inflate(R.layout.dialog_red_packet_info,
                null);
        Dialog dialog_cancel = new Dialog(context, R.style.transparentFrameWindowStyle);
        dialog_cancel.setContentView(view_red_packet, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ImageView iv_dialog_close = view_red_packet.findViewById(R.id.iv_dialog_close);
        TextView tv_know = view_red_packet.findViewById(R.id.tv_know);
        iv_dialog_close.setOnClickListener(view1 -> dialog_cancel.dismiss());
        tv_know.setOnClickListener(view12 -> dialog_cancel.dismiss());
        Window window = dialog_cancel.getWindow();
        /**
         * 位于底部
         */
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        /**
         * 设置弹出动画
         */
        window.setWindowAnimations(R.style.ani_bottom);
        // 设置点击外围解散
        dialog_cancel.setCanceledOnTouchOutside(true);
        dialog_cancel.show();
    }

    /**
     * 激活vip对话框
     */
    public static void showRedPacketAgreement(Activity context) {
        View view = context.getLayoutInflater().inflate(R.layout.dialog_activate_vip,
                null);
        final Dialog dialog_message = new Dialog(context, R.style.transparentFrameWindowStyle);
        dialog_message.setContentView(view, new ViewGroup.LayoutParams(ScreenUtils.dip2px(context, 295),
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // 设置点击外围解散
        dialog_message.setCanceledOnTouchOutside(true);
        TextView tv_old_user_remain = view.findViewById(R.id.tv_old_user_remain);
        long remain = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class).getPointE2();
        tv_old_user_remain.setText("您的平移金额：¥" + StringUtils.getAllPrice(remain));
        ImageView iv_dialog_close = view.findViewById(R.id.iv_dialog_close);
        ImageView iv_activate_vip_btn = view.findViewById(R.id.iv_activate_vip_btn);
        iv_activate_vip_btn.setOnClickListener(view12 -> {
            dialog_message.dismiss();
            context.startActivity(new Intent(context, MemberVipGiftBuyActivity.class));
        });
        iv_dialog_close.setOnClickListener(view1 -> dialog_message.dismiss());
        dialog_message.show();
    }


    public static void getShareDialog(final Activity context,
                                      final UMImage image, final String webpageUrl, final String title, final String description, final UMShareListener umShareListener) {

        View view = context.getLayoutInflater().inflate(R.layout.dialog_share_choose,
                null);
        final Dialog dialog = new Dialog(context, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        /**
         * 位于底部
         */
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        /**
         * 设置弹出动画
         */
        window.setWindowAnimations(R.style.ani_bottom);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        TextView btn_weixin = view.findViewById(R.id.btn_weixin);
        TextView btn_weixin_chat = view.findViewById(R.id.btn_weixin_chat);
        TextView btn_qq = view.findViewById(R.id.btn_qq);
        TextView btn_qq_chat = view.findViewById(R.id.btn_qq_chat);
        TextView btn_sina = view.findViewById(R.id.btn_sina);
        TextView btn_cancel = view.findViewById(R.id.tv_cancel);
        final UMWeb web = new UMWeb(webpageUrl);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(description);//描述
        btn_weixin.setOnClickListener(view13 -> {  //微信好友
            dialog.dismiss();
            new ShareAction(context).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                    .withMedia(web)
                    .share();
        });
        btn_weixin_chat.setOnClickListener(view12 -> {  //朋友圈
            dialog.dismiss();

            new ShareAction(context).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                    .withMedia(web)
                    .share();
        });
        btn_qq.setOnClickListener(view12 -> {  //朋友圈
            dialog.dismiss();

            new ShareAction(context).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
                    .withMedia(web)
                    .share();
        });
        btn_qq_chat.setOnClickListener(view12 -> {  //朋友圈
            dialog.dismiss();

            new ShareAction(context).setPlatform(SHARE_MEDIA.QZONE).setCallback(umShareListener)
                    .withMedia(web)
                    .share();
        });
        btn_sina.setOnClickListener(view12 -> {  //朋友圈
            dialog.dismiss();

            new ShareAction(context).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
                    .withMedia(web)
                    .share();
        });
        btn_cancel.setOnClickListener(view1 -> dialog.dismiss());

    }

    /**
     * 邀请好友界面分享对话框
     *
     * @param context
     * @param image
     * @param webpageUrl
     * @param title
     * @param description
     * @param codeUrl  海报上二维码
     * @param umShareListener
     */
    private static int select_share_type = 1;  //分享类型（1：分享海报  2：分享链接）
    private static UMImage image_pic;

    public static void getInviteFriendShareDialog(final Activity context,
                                                  final UMImage image, final String webpageUrl, final String title, final String description, final String codeUrl, final UMShareListener umShareListener) {
        select_share_type = 2;
        View view = context.getLayoutInflater().inflate(R.layout.dialog_invite_friend_share_choose,
                null);
        Dialog dialog = new Dialog(context, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        /**
         * 位于底部
         */
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        /**
         * 设置弹出动画
         */
        window.setWindowAnimations(R.style.ani_bottom);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        RelativeLayout rl_share_pic = view.findViewById(R.id.rl_share_pic);
        TextView tv_inviter_id = view.findViewById(R.id.tv_inviter_id);
        tv_inviter_id.setText("邀请 ID：" + PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class).getUserId());
        ImageView sdv_pic_qr = view.findViewById(R.id.iv_code);
        if (!StringUtils.isTextEmpty(codeUrl)) {
            Glide.with(context).load(codeUrl).into(sdv_pic_qr);
            // sdv_pic_qr.setImageURI(Uri.parse(codeUrl));
        }
        TextView btn_share_pic = view.findViewById(R.id.btn_share_pic);
        TextView btn_share_link = view.findViewById(R.id.btn_share_link);

        TextView btn_weixin = view.findViewById(R.id.btn_weixin);
        TextView btn_weixin_chat = view.findViewById(R.id.btn_weixin_chat);
        TextView btn_qq = view.findViewById(R.id.btn_qq);
        TextView btn_qq_chat = view.findViewById(R.id.btn_qq_chat);
        TextView btn_sina = view.findViewById(R.id.btn_sina);

        TextView btn_cancel = view.findViewById(R.id.tv_cancel);

        dialog.setOnDismissListener(dialog1 -> view.destroyDrawingCache());

        dialog.show();

        final UMWeb web = new UMWeb(webpageUrl);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(description);//描述

        rl_share_pic.setDrawingCacheEnabled(true);
        rl_share_pic.buildDrawingCache();

        //选择分享海报
        btn_share_pic.setOnClickListener(v -> {
            rl_share_pic.setVisibility(View.VISIBLE);
            select_share_type = 1;
            btn_share_pic.setBackgroundResource(R.drawable.round_colorwhite_tr_16);
            btn_share_pic.setTextColor(SCApp.getInstance().getRes().getColor(R.color.color_text1));
            btn_share_link.setBackgroundResource(R.drawable.round_color_stroke5_tl_16);
            btn_share_link.setTextColor(SCApp.getInstance().getRes().getColor(R.color.color_text6));

        });

        //选择分享链接
        btn_share_link.setOnClickListener(v -> {
            rl_share_pic.setVisibility(View.GONE);
            select_share_type = 2;
            btn_share_pic.setBackgroundResource(R.drawable.round_color_stroke5_tr_16);
            btn_share_pic.setTextColor(SCApp.getInstance().getRes().getColor(R.color.color_text6));
            btn_share_link.setBackgroundResource(R.drawable.round_colorwhite_tl_16);
            btn_share_link.setTextColor(SCApp.getInstance().getRes().getColor(R.color.color_text1));

        });

        btn_weixin.setOnClickListener(view13 -> {  //微信好友
            //dialog.dismiss();
            if (select_share_type == 1) {

                sharePosterAction(context, rl_share_pic, SHARE_MEDIA.WEIXIN, title, umShareListener);

            } else {
                new ShareAction(context).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                        .withMedia(web)
                        .share();
            }
        });
        btn_weixin_chat.setOnClickListener(view12 -> {  //朋友圈
            //dialog.dismiss();
            if (select_share_type == 1) {
                sharePosterAction(context, rl_share_pic, SHARE_MEDIA.WEIXIN_CIRCLE, title, umShareListener);
            } else {
                new ShareAction(context).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                        .withMedia(web)
                        .share();
            }
        });
        btn_qq.setOnClickListener(view13 -> {  //QQ
            //dialog.dismiss();
            if (select_share_type == 1) {
                sharePosterAction(context, rl_share_pic, SHARE_MEDIA.QQ, title, umShareListener);
            } else {
                new ShareAction(context).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
                        .withMedia(web)
                        .share();
            }
        });
        btn_qq_chat.setOnClickListener(view12 -> {  //QQ空间
            //dialog.dismiss();
            if (select_share_type == 1) {
                sharePosterAction(context, rl_share_pic, SHARE_MEDIA.QZONE, title, umShareListener);
            } else {
                new ShareAction(context).setPlatform(SHARE_MEDIA.QZONE).setCallback(umShareListener)
                        .withMedia(web)
                        .share();
            }
        });
        btn_sina.setOnClickListener(view12 -> {  //sina微博
            //dialog.dismiss();
            if (select_share_type == 1) {
                sharePosterAction(context, rl_share_pic, SHARE_MEDIA.SINA, title, umShareListener);
            } else {
                new ShareAction(context).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
                        .withMedia(web)
                        .share();
            }
        });


        btn_cancel.setOnClickListener(view1 -> dialog.dismiss());

    }

    public static void sharePosterAction(Activity context, RelativeLayout rl_share_pic, SHARE_MEDIA platfrom, String title, UMShareListener umShareListener) {
        rl_share_pic.post(() -> {
            Bitmap bitmap = BitmapUtils.loadBitmapFromView(rl_share_pic);
            UMImage thumb = new UMImage(context, bitmap);
            UMImage img = new UMImage(context, bitmap);
            img.setThumb(thumb);
            img.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
            img.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
            new ShareAction(context).setPlatform(platfrom).withText(title).setCallback(umShareListener)
                    .withMedia(img).share();
        });
    }


    /**
     * 发票须知对话框
     */
    public static void showInvoiceIntroduce(Activity context) {
        View view = context.getLayoutInflater().inflate(R.layout.dialog_invoice_tip_introduce,
                null);
        final Dialog dialog_message = new Dialog(context, R.style.transparentFrameWindowStyle);
        dialog_message.setContentView(view, new ViewGroup.LayoutParams(ScreenUtils.dip2px(context, 315),
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // 设置点击外围解散
        dialog_message.setCanceledOnTouchOutside(true);
        TextView tv_know = view.findViewById(R.id.tv_know);
        tv_know.setOnClickListener(view1 -> dialog_message.dismiss());
        dialog_message.show();
    }

    public static void showRpTakeDialog(Activity activity, RpDetailRes data, RedClickListener mListener) {
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_rp_grab, null);
        Dialog dialog_rp = new Dialog(activity, R.style.transparentFrameWindowStyle);
        dialog_rp.setContentView(view);
        dialog_rp.setCanceledOnTouchOutside(true);
        ImageView iv_close = view.findViewById(R.id.iv_close);
        ImageView iv_open_rp = view.findViewById(R.id.iv_open_rp); //点击按钮
        ImageView iv_header = view.findViewById(R.id.iv_header); //头像
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_tip = view.findViewById(R.id.tv_tip);
        tv_tip.setText(data.showInfo);
        TextView tv_look_others = view.findViewById(R.id.tv_look_others);
        iv_close.setOnClickListener(v -> dialog_rp.dismiss());
        iv_open_rp.setOnClickListener(v -> {
            //抢
            if (mListener != null) {
                mListener.onRpGarb();
            }
        });
        tv_look_others.setOnClickListener(v -> {
            //查看详情
            if (mListener != null) {
                mListener.onRpDetail();
            }
        });
        dialog_rp.show();

    }

    public static void showRpOutOfDate(Activity activity) {
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_rp_out_date, null);
        Dialog dialog = new Dialog(activity, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view);
        ImageView iv_close = view.findViewById(R.id.iv_close);
        TextView tv_tip = view.findViewById(R.id.tv_tip);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

}
