package handbank.hbwallet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {
    protected lateinit var mViewModel: VM
    protected var mRoot: View? = null
    protected var mInflater: LayoutInflater? = null
    protected var mFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initVM()
        initView()
        initData()
        startObserve()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(), null)
    }

    open fun startObserve() {}

    abstract fun getLayoutResId(): Int
    abstract fun initView()
    abstract fun initData()
    fun onRestartInstance(bundle: Bundle) {

    }

    override fun onResume() {
        super.onResume()
        Log.i("ActivityManage:", this.javaClass.name) // 打印出每个activity的类名


    }

    private fun initVM() {
        providerVMClass()?.let {
            mViewModel = ViewModelProviders.of(this).get(it)
            mViewModel.let(lifecycle::addObserver)
        }
    }

    open fun providerVMClass(): Class<VM>? = null

    protected fun startActivity(z: Class<*>) {
        startActivity(Intent(activity, z))
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
    }

    /**
     * 添加
     *
     * @param frameLayoutId
     * @param fragment
     */
    protected open fun addFragment(frameLayoutId: Int, fragment: Fragment?) {
        if (fragment != null) {
            val transaction = activity!!.supportFragmentManager.beginTransaction()
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

}