package cn.sancell.xingqiu.kt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import cn.sancell.xingqiu.R
import cn.sancell.xingqiu.base.ServerErrorActivity
import cn.sancell.xingqiu.constant.network.NetWorkMonitorManager
import cn.sancell.xingqiu.constant.network.NetWorkState
import cn.sancell.xingqiu.constant.network.onNetWorkStateChangeLinsener
import cn.sancell.xingqiu.live.dialog.ComfirmDialog
import cn.sancell.xingqiu.util.observer.ObserverKey
import cn.sancell.xingqiu.util.observer.ObserverManger
import cn.sancell.xingqiu.util.observer.OnObserver
import cn.sancell.xingqiu.widget.Loading_view
import handbank.hbwallet.BaseViewModel
import kotlinx.android.synthetic.main.toolbar_base.*

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity(), onNetWorkStateChangeLinsener {
    protected lateinit var mViewModel: VM
    protected var mFragment: Fragment? = null
    private var alterDialog: ComfirmDialog? = null
    private var mLoadingView: Loading_view? = null
    var isSetView = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isSetView) {
            setContentView(getLayoutResId())
        }
        setWindeMode()
        NetWorkMonitorManager.getInstance().register(this)
        ObserverManger.getInstance(ObserverKey.SERVER_ERROR).registerObserver(mServerEorrorObserver)
        initVM()
        initView()
        setSupportActionBar(mToolbar)
        initData()
        startObserve()
    }

    //用来设置windows的属性
    open fun setWindeMode() {

    }

    open fun startObserve() {}

    abstract fun getLayoutResId(): Int
    abstract fun initView()
    abstract fun initData()

    private fun initVM() {
        providerVMClass()?.let {
            mViewModel = ViewModelProviders.of(this).get(it)
            mViewModel.let(lifecycle::addObserver)
        }
    }

    override fun onNetWorkStateChangeLinener(netWorkState: NetWorkState?) {
        if (netWorkState == NetWorkState.NONE) {
            if (alterDialog == null) {
                alterDialog = NetWorkMonitorManager.getInstance().getAlterDialog(this)
            }
            if (!alterDialog!!.isShowing) {
                alterDialog?.show()
            }
        } else {
            if (alterDialog != null && alterDialog!!.isShowing) {
                alterDialog?.dismiss()
            }

        }
    }

    private val mServerEorrorObserver = OnObserver { startActivity(Intent(this@BaseActivity, ServerErrorActivity::class.java)) }
    override fun onResume() {
        super.onResume()
        Log.i("ActivityManage:", this.javaClass.name) // 打印出每个activity的类名

    }

    /**
     * 添加
     *
     * @param frameLayoutId
     * @param fragment
     */
    protected open fun addFragment(frameLayoutId: Int, fragment: Fragment?) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            if (fragment.isAdded) {
                if (mFragment != null) {
                    transaction.hide(mFragment!!).show(fragment)
                } else {
                    transaction.show(fragment)
                }
            } else {
                if (mFragment != null) {
                    transaction.hide(mFragment!!).add(frameLayoutId, fragment)
                } else {
                    transaction.add(frameLayoutId, fragment)
                }
            }
            mFragment = fragment
            transaction.commit()
        }
    }

    open fun providerVMClass(): Class<VM>? = null

    protected fun startActivity(z: Class<*>) {
        startActivity(Intent(this, z))
    }

    //加载
    protected fun showLoading(show: Boolean) {
        if (mLoadingView == null) {
            mLoadingView = Loading_view(this, R.style.LoadingDialog)
        }
        if (show) {
            mLoadingView!!.show()
        } else {
            mLoadingView!!.dismiss()
        }
    }

    protected fun showProgressDialog(content: String) {

    }

    protected fun showProgressDialog(resId: Int) {

    }

    protected fun dismissProgressDialog() {
    }

    override fun onDestroy() {
        if (::mViewModel.isInitialized) {
            mViewModel.let {
                lifecycle.removeObserver(it)
            }
        }
        super.onDestroy()
        NetWorkMonitorManager.getInstance().unregister(this)
        ObserverManger.getInstance(ObserverKey.SERVER_ERROR).removeObserver(mServerEorrorObserver)
    }
}