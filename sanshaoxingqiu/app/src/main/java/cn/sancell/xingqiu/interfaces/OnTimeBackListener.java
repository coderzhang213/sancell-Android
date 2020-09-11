package cn.sancell.xingqiu.interfaces;


/**
  * @author Alan_Xiong
  *
  * @desc:
  * @time 2020-01-04 14:54
  */
public interface OnTimeBackListener {

    void onTick(String hour, String minute, String second);

    void onFinish();
}
