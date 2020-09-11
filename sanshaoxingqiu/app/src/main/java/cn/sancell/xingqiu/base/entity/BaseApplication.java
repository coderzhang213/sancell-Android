package cn.sancell.xingqiu.base.entity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import cn.sancell.xingqiu.constant.network.NetStateUtils;
import cn.sancell.xingqiu.util.EToast2;

public abstract class BaseApplication extends MultiDexApplication {
  public static BaseApplication myApp;
  public static final int TIMEOUT = 60;

  private static BaseApplication sSelf = null;

  public static Context context;
  private static Resources res = null;

  /**
   * 全局的 handler 对象
   */
  private final Handler mAppHandler = new Handler();
  private ArrayList<Activity> mActivities = new ArrayList<>();

  private Typeface typeface;
  private Typeface typeface_regular;

  @Override
  public void onCreate() {
    super.onCreate();
    registerActivityListener();
    myApp = this;
    context = myApp.getApplicationContext();
    typeface = Typeface.createFromAsset(getInstance().getAssets(), "fonts/dinMedium.ttf");
    typeface_regular = Typeface.createFromAsset(getInstance().getAssets(), "fonts/dinRegular.ttf");
    String processName = NetStateUtils.getProcessName();
    if (!TextUtils.isEmpty(processName) && processName.equals(this.getPackageName())) {//只在主进程初始化
      mainInitApp();
    }
    initApp();
  }

  protected abstract void mainInitApp();

  protected abstract void initApp();

  protected abstract void uninitApp();

  public static Context getContext() {
    return context;
  }

  public BaseApplication() {
    sSelf = this;
  }

  public static BaseApplication getInstance() {
    return sSelf;
  }


  public Handler getAppHandler() {
    return mAppHandler;
  }

// for toast use conveniently


  /**
   * 展示app toast，不依赖于activity的生命周期
   *
   * @param msg
   */
  public void showSystemCenterToast(String msg) {
    try {
      cn.sancell.xingqiu.util.EToast2.makeText(currentActivity(), msg, EToast2.LENGTH_SHORT).show();
    } catch (Exception e) {

    }
  }


  public void showSystemCenterToast(int id) {
    showSystemCenterToast(getRes().getString(id));
  }


  public Typeface getTypeface() {
    return typeface;
  }

  public void setTypeface(Typeface typeface) {
    this.typeface = typeface;
  }

  public Typeface getTypefaceRegular() {
    return typeface_regular;
  }

  public void setTypefaceRegular(Typeface typeface) {
    this.typeface_regular = typeface;
  }

  /**
   * 获取Resource
   *
   * @return
   */
  public Resources getRes() {
    if (res == null) {
      res = context.getResources();
    }
    return res;
  }

  private void registerActivityListener() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
          /**
           *  监听到 Activity创建事件 将该 Activity 加入list
           */
          holdActivity(activity);

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
          if (null == mActivities && mActivities.isEmpty()) {
            return;
          }
          if (mActivities.contains(activity)) {
            /**
             *  监听到 Activity销毁事件 将该Activity 从list中移除
             */
            releaseActivity(activity);
          }
        }
      });
    }
  }

  public void holdActivity(Activity activity) {
    mActivities.add(activity);
  }

  public void releaseActivity(Activity activity) {
    mActivities.remove(activity);
  }

  /**
   * get current Activity 获取当前Activity（栈中最后一个压入的）
   */
  public Activity currentActivity() {
    if (mActivities == null || mActivities.isEmpty()) {
      return null;
    }
    Activity activity = mActivities.get(mActivities.size() - 1);
    return activity;
  }

  public Activity getTopActivity() {
    if (mActivities.size() == 0) {
      return null;
    }
    return mActivities.get(mActivities.size() - 1);
  }

  public void finishAllActivities() {
    final ArrayList<Activity> tmp = new ArrayList<Activity>();
    tmp.addAll(mActivities);
    mActivities.clear();
    for (Activity activity : tmp) {
      if (activity != null) {
        activity.finish();
      }
    }
    tmp.clear();
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    //fixOPPOTimeout();
  }

  /**
   * 解决oppo GC TimeoutExceptions
   */
  public void fixOPPOTimeout() {
    try {
      final Class clazz = Class.forName("java.lang.Daemon$FinalizerWatchdogDaemon");
      final Field field = clazz.getDeclaredField("INSTANCE");
      field.setAccessible(true);
      final Object watch = field.get(null);
      try {
        final Field thread = clazz.getSuperclass().getDeclaredField("Thread");
        thread.setAccessible(true);
        thread.set(watch, null);
      } catch (final Throwable t) {
        Log.e("TAG", "stopWatchDog: set null error" + t);
        t.printStackTrace();
        try {
          final Method method = clazz.getSuperclass().getDeclaredMethod("stop");
          method.setAccessible(true);
          method.invoke(watch);
        } catch (final Throwable e) {
          Log.e("TAG", "stopWatchDog: stop error" + t);
          t.printStackTrace();
        }
      }

    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }


}