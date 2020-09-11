package cn.sancell.xingqiu.bean;


import java.util.List;

/**
  * @author Alan_Xiong
  *
  * @desc: 代金券
  * @time 2020/5/9 3:42 PM
  */
public class OrderVoucherRes {

    public int validCount; //有效count
    public Valid canUsed;

    public Invalid invalid;

    public static class Valid{
        public List<VoucherInfo> dataList;
        public int dataCount;
    }

    public static class Invalid{
        public List<VoucherInfo> dataList;
        public int dataCount;
    }


}
