package cn.sancell.xingqiu.constant.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by zj on 2019/12/16.
 */
public class NetStateUtils {
  /**
   * 获取当前的网络状态 ：没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
   * 自定义
   *
   * @param context
   * @return
   */
  public static int getAPNType(Context context) {
    //结果返回值
    int netType = 0;
    //获取手机所有连接管理对象
    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context
            .CONNECTIVITY_SERVICE);
    //获取NetworkInfo对象
    NetworkInfo networkInfo = manager.getActiveNetworkInfo();
    //NetworkInfo对象为空 则代表没有网络
    if (networkInfo == null) {
      return netType;
    }
    //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
    int nType = networkInfo.getType();
    if (nType == ConnectivityManager.TYPE_WIFI) {
      //WIFI
      netType = 1;
    } else if (nType == ConnectivityManager.TYPE_MOBILE) {
      int nSubType = networkInfo.getSubtype();
      TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService
              (Context.TELEPHONY_SERVICE);
      //3G   联通的3G为UMTS或HSDPA 电信的3G为EVDO
      if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
              && !telephonyManager.isNetworkRoaming()) {
        netType = 4;
      } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
              || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
              || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
              && !telephonyManager.isNetworkRoaming()) {
        netType = 3;
        //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
      } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
              || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
              || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
              && !telephonyManager.isNetworkRoaming()) {
        netType = 2;
      } else {
        netType = 2;
      }
    }
    return netType;
  }

  public static String getProcessName() {
    try {
      File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
      BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
      String processName = mBufferedReader.readLine().trim();
      mBufferedReader.close();
      return processName;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
