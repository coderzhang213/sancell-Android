package cn.sancell.xingqiu.im.ui.addressBook;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.base.BaseMVPToobarActivity;
import cn.sancell.xingqiu.base.mvp.BaseView;
import cn.sancell.xingqiu.constant.UiHelper;
import cn.sancell.xingqiu.im.ui.addressBook.fragment.AddressBookFragment;
import cn.sancell.xingqiu.im.ui.createTeam.CreateTeamActivity;
import cn.sancell.xingqiu.im.entity.TabEntity;
import cn.sancell.xingqiu.im.ui.findTeam.FindTeamActivity;
import cn.sancell.xingqiu.im.manager.SystemMessageUnreadManager;
import cn.sancell.xingqiu.im.ui.sysMsg.SystemMessageActivity;
import cn.sancell.xingqiu.util.StatusBarUtil;

/**
  * @author Alan_Xiong
  *
  * @desc: im 通讯录
  * @time 2019-11-13 22:23
  */
public class AddressBookActivity extends BaseMVPToobarActivity<AddressBookPresenter> implements AddressBookView, View.OnClickListener {

    @BindView(R.id.tv_create_team)
    AppCompatTextView tv_create_team;
    @BindView(R.id.tv_find_team)
    AppCompatTextView tv_find_team;
    @BindView(R.id.tv_message)
    AppCompatTextView tv_message;
    @BindView(R.id.v_message_dot)
    View v_message_dot;
    @BindView(R.id.vp_book)
    ViewPager vp_book;
    @BindView(R.id.tb_book)
    TabLayout tb_book;

    public static void start(Context context){
        Intent intent = new Intent(context,AddressBookActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected AddressBookPresenter createPresenter() {
        return new AddressBookPresenter(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_im_address_book;
    }

    @Override
    protected void initial() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initActivityTitle("通讯录");
        initSysMsgObservers(true);
        List<TabEntity> tabs = new ArrayList<>();
        tabs.add(new TabEntity(getResources().getString(R.string.im_me_create_team), UiHelper.TEAM_TYPE_CREATE));
        tabs.add(new TabEntity(getResources().getString(R.string.im_join_team), UiHelper.TEAM_TYPE_JOIN));
        List<Fragment> mFragments = new ArrayList<>();
        for (TabEntity tab:tabs){
            tb_book.addTab(tb_book.newTab());
            mFragments.add(AddressBookFragment.newInstance(tab.tab_type));
        }

        vp_book.setAdapter(new NormalPageAdapter(getSupportFragmentManager(), mFragments,tabs));
        vp_book.setCurrentItem(0);
        tv_create_team.setOnClickListener(this);
        tv_find_team.setOnClickListener(this);
        tv_message.setOnClickListener(this);

    }


    /**
     * 注册系统消息监听
     * @param register 是否注册
     */
    public void initSysMsgObservers(boolean register){

        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(
                sysMsgUnreadCountChangedObserver, register);
    }

    private Observer<Integer> sysMsgUnreadCountChangedObserver = (Observer<Integer>) unreadCount -> {
        //系统消息提醒未读数量
        SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unreadCount);
        if (unreadCount > 0){
            v_message_dot.setVisibility(View.VISIBLE);
        }else{
            v_message_dot.setVisibility(View.GONE);
        }
    };

    @Override
    protected BaseView getView() {
        return this;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_create_team){
            //创建群组
            CreateTeamActivity.start(this);
        }else if (v.getId() == R.id.tv_find_team){
            //查找群组，uikit提供
            FindTeamActivity.start(this);

        }else if (v.getId() == R.id.tv_message){
            //消息
            SystemMessageActivity.start(this);//系统消息
        }
    }

    @Override
    protected void onDestroy() {
        //取消注册
        initSysMsgObservers(false);
        super.onDestroy();
    }

}
