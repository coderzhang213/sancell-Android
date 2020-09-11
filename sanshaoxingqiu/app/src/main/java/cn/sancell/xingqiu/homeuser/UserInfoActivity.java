package cn.sancell.xingqiu.homeuser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.netease.nim.uikit.common.media.imagepicker.loader.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.glide.ImageLoaderUtils;
import cn.sancell.xingqiu.homeuser.bean.UpLoadPhotoInfoBean;
import cn.sancell.xingqiu.homeuser.contract.UserInfoContract;
import cn.sancell.xingqiu.login.bean.UserBean;
import cn.sancell.xingqiu.util.DateUtils;
import cn.sancell.xingqiu.util.DialogUtil;
import cn.sancell.xingqiu.util.MyOSSUtils;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.StatusBarUtil;
import cn.sancell.xingqiu.util.StringUtils;

public class UserInfoActivity extends BaseMVPToobarActivity<UserInfoContract.UserInfoPresenter> implements UserInfoContract.UserInfoView,
        View.OnClickListener {
    @BindView(R.id.rl_photo)
    RelativeLayout rl_photo;
    @BindView(R.id.riv_user_photo)
    AppCompatImageView riv_user_photo;
    @BindView(R.id.rl_nickname)
    RelativeLayout rl_nickname;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.rl_sex)
    RelativeLayout rl_sex;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.rl_birthday)
    RelativeLayout rl_birthday;
    @BindView(R.id.tv_birthday)
    TextView tv_birthday;


    UserBean userBean;

    private String mSelectImgPath;


    @Override
    protected UserInfoContract.UserInfoPresenter createPresenter() {
        return new UserInfoContract.UserInfoPresenter();
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initial() {
        ButterKnife.bind(this);
        initActivityTitle(R.string.userinfo_title);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.topMargin = statusHeight;
            toolbar.setLayoutParams(lp);
        }
        rl_photo.setOnClickListener(this);
        rl_nickname.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_birthday.setOnClickListener(this);
        mPresenter.GetUserInfo(this);
    }


    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void toast(String text) {
        SCApp.getInstance().showSystemCenterToast(text);
    }

    @Override
    public void netWorkError() {
        mNetworkErrorLayout.setVisibility(View.VISIBLE);
        tv_refresh.setOnClickListener(view -> mPresenter.GetUserInfo(UserInfoActivity.this));
    }

    @Override
    public void getUserInfoSuccess() {
        mNetworkErrorLayout.setVisibility(View.GONE);
        refreshUserInfo();
    }

    public void refreshUserInfo() {
        userBean = PreferencesUtils.readObject(Constants.Key.KEY_USERINFO, UserBean.class);
        if (StringUtils.isTextEmpty(userBean.getNickname())) {
            tv_nickname.setText(R.string.input_nick_name);
            tv_nickname.setTextColor(getResources().getColor(R.color.color_text4));
        } else {
            tv_nickname.setText(userBean.getNickname());
            tv_nickname.setTextColor(getResources().getColor(R.color.color_text1));
        }
        if (StringUtils.isTextEmpty(userBean.getUserInfo().getBirthday())) {
            tv_birthday.setText(R.string.input_birthday);
            tv_birthday.setTextColor(getResources().getColor(R.color.color_text4));
        } else {
            tv_birthday.setText(DateUtils.getStrTime(userBean.getUserInfo().getBirthday()));
            tv_birthday.setTextColor(getResources().getColor(R.color.color_text1));
        }
        if (userBean.getUserInfo().getGender() == 0) {
            tv_sex.setText(R.string.select_sex);
            tv_sex.setTextColor(getResources().getColor(R.color.color_text4));
        } else {
            tv_sex.setText(userBean.getUserInfo().getGender() == 1 ? R.string.sex_male : R.string.sex_female);
            tv_sex.setTextColor(getResources().getColor(R.color.color_text1));
        }
        ImageLoaderUtils.loadCircleImage(UserInfoActivity.this,userBean.getGravatar(),riv_user_photo);
    }

    @Override
    public void modifyNickNameSuccess() {
        mPresenter.GetUserInfo(UserInfoActivity.this);
    }

    @Override
    public void modifySexSuccess() {
        mPresenter.GetUserInfo(UserInfoActivity.this);
    }

    @Override
    public void modifyBirthdaySuccess() {
        mPresenter.GetUserInfo(UserInfoActivity.this);
    }


    @Override
    public void getuploadPhotoInfoSuccess(UpLoadPhotoInfoBean upLoadPhotoInfoBean) {
        String bucketName = upLoadPhotoInfoBean.getBackName();
        String objectKey = upLoadPhotoInfoBean.getKeyName();
        String accessKeyId = upLoadPhotoInfoBean.getAccessKeyId();
        String accessKeySecret = RSAUtils.decryptByPublic(upLoadPhotoInfoBean.getAccessKeySecret());
        String securityToken = upLoadPhotoInfoBean.getSecurityToken();
        MyOSSUtils.getInstance().getOSs(UserInfoActivity.this, upLoadPhotoInfoBean.getEndpoint(), accessKeyId, accessKeySecret, securityToken);
        MyOSSUtils.getInstance().upImage(UserInfoActivity.this, new MyOSSUtils.OssUpCallback() {
            @Override
            public void successImg(String img_url) {
                riv_user_photo.post(() -> ImageLoaderUtils.loadCircleImage(UserInfoActivity.this,img_url,riv_user_photo));

                Log.e("successImg",img_url);
            }

            @Override
            public void successVideo(String video_url) {

            }

            @Override
            public void inProgress(long progress, long zong) {

            }
        }, bucketName, objectKey, mSelectImgPath);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_photo:
                DialogUtil.showModifyPhoto(UserInfoActivity.this);
                break;
            case R.id.rl_nickname:
                if (userBean != null) {
                    DialogUtil.showModifyNickName(UserInfoActivity.this, clickNickNameCommitAction, userBean.getNickname());
                }
                break;
            case R.id.rl_sex:
                if (userBean != null) {
                    DialogUtil.showModifySex(UserInfoActivity.this, clickSexCommitAction, userBean.getUserInfo().getGender());
                }
                break;
            case R.id.rl_birthday:
                DialogUtil.onYearMonthDayPicker(UserInfoActivity.this, getDateAction);
                break;
        }
    }


    DialogUtil.GetDateAction getDateAction = new DialogUtil.GetDateAction() {
        @Override
        public void sureAction(String year, String month, String day) {
            mPresenter.ModifyBirthday(DateUtils.getStringToDate(year + getResources().getString(R.string.year) + month + getResources().getString(R.string.month) + day + getResources().getString(R.string.day)), UserInfoActivity.this);
        }
    };


    DialogUtil.ClickCommitAction clickNickNameCommitAction = new DialogUtil.ClickCommitAction() {
        @Override
        public void commitAction(String reason) {
            mPresenter.ModifyNickName(reason, UserInfoActivity.this);
        }

        @Override
        public void commitAction(String select_reason, int postion) {

        }
    };

    DialogUtil.ClickCommitAction clickSexCommitAction = new DialogUtil.ClickCommitAction() {
        @Override
        public void commitAction(String reason) {
            mPresenter.ModifySex(reason, UserInfoActivity.this);
        }

        @Override
        public void commitAction(String select_reason, int postion) {

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                //AndroidX路径为新的字段
                List<LocalMedia> mPictures = PictureSelector.obtainMultipleResult(data);
                if (mPictures != null) {
                    Bitmap bitmap;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        mSelectImgPath = mPictures.get(0).getAndroidQToPath();
                        bitmap = BitmapFactory.decodeFile(mPictures.get(0).getAndroidQToPath());
                    }else{
                        mSelectImgPath = mPictures.get(0).getPath();
                        bitmap = BitmapFactory.decodeFile(mPictures.get(0).getPath());
                    }
                    if (bitmap != null) {

                        mPresenter.GetUpLoadPhotoInfo(bitmap.getWidth() + "", bitmap.getHeight() + "", bitmap.getByteCount() / 1024 + "", UserInfoActivity.this);
                    }
                }
            }else if (requestCode == PictureConfig.REQUEST_CAMERA){
                List<LocalMedia> mPictures = PictureSelector.obtainMultipleResult(data);
                if (mPictures != null) {
                    Bitmap bitmap;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        mSelectImgPath = mPictures.get(0).getAndroidQToPath();
                        bitmap = BitmapFactory.decodeFile(mPictures.get(0).getAndroidQToPath());
                    }else{
                        mSelectImgPath = mPictures.get(0).getPath();
                        bitmap = BitmapFactory.decodeFile(mPictures.get(0).getPath());
                    }
                    if (bitmap != null) {
                        mPresenter.GetUpLoadPhotoInfo(bitmap.getWidth() + "", bitmap.getHeight() + "", bitmap.getByteCount() / 1024 + "", UserInfoActivity.this);
                    }
                }
            }
        }
    }
}
