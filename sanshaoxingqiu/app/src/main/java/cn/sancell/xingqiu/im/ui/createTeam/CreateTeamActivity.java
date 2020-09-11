package cn.sancell.xingqiu.im.ui.createTeam;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import butterknife.BindView;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.im.entity.req.TeamCreateReq;
import cn.sancell.xingqiu.im.entity.res.TeamCreateRes;
import cn.sancell.xingqiu.im.sys.SessionHelper;
import cn.sancell.xingqiu.im.ui.red.call.ScClient;
import cn.sancell.xingqiu.util.StatusBarUtil;

/**
  * @author Alan_Xiong
  *
  * @desc: 创建群组
  * @time 2019-11-13 18:58
  */
public class CreateTeamActivity extends BaseMVPToobarActivity<CreateTeamPresenter> implements CreateTeamView {

    @BindView(R.id.et_team_name)
    AppCompatEditText et_team_name;
    @BindView(R.id.et_introduce)
    AppCompatEditText et_introduce;
    @BindView(R.id.et_announce)
    AppCompatEditText et_announce;
    @BindView(R.id.btn_sure)
    AppCompatButton btn_sure;

    public static void start(Context context){
        Intent intent = new Intent(context,CreateTeamActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected CreateTeamPresenter createPresenter() {
        return new CreateTeamPresenter(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_im_create_team;
    }

    @Override
    protected void initial() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initActivityTitle(getString(R.string.im_create_team));
        initListener();
        btn_sure.setOnClickListener(v -> createTeamReq());
    }

    public void initListener(){
        et_team_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)){
                    btn_sure.setEnabled(true);
                }else{
                    btn_sure.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    public void createTeamReq(){
        if (TextUtils.isEmpty(et_team_name.getText().toString())){
            SCApp.getInstance().showSystemCenterToast("请输入群名称");
            return;
        }
        TeamCreateReq req = new TeamCreateReq();
        req.announcement = et_announce.getText().toString();
        req.intro = et_introduce.getText().toString();
        req.title = et_team_name.getText().toString();
        mPresenter.createTeam(req);
    }

    @Override
    public void teamCreateSuccess(TeamCreateRes res) {
        if (res != null){
            SCApp.getInstance().showSystemCenterToast("创建成功");
            ScClient.enterTeamChat(this,res.tid);
            finish();
        }else{
            SCApp.getInstance().showSystemCenterToast("创建失败");
        }
    }

    @Override
    public void teamCreateError(String error) {
        SCApp.getInstance().showSystemCenterToast(error);
    }
}
