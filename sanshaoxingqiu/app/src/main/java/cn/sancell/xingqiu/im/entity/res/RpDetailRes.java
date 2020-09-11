package cn.sancell.xingqiu.im.entity.res;

import java.util.List;

import cn.sancell.xingqiu.util.BigDecimalUtils;

/**
  * @author Alan_Xiong
  *
  * @desc: 红包详情
  * @time 2019-11-18 13:40
  */
public class RpDetailRes {

    public String grabMoney;
    public String redUserName;
    public int redReceviceSumNum;

    public String redStatus;
    public int redType;
    public String showInfo;

    public String redSingleMoney;
    public int redNum;

    public List<ReceiveDetail> receiveDetail;

    public int receiveDetailCount;

    public static class ReceiveDetail{

        public String getUserId;

        public String getUserName;
        public String getMoney;
        public String getUserIcon;
        public String receiveDate;
        public boolean isSkyChild;

        public String getGetMoney() {
            return BigDecimalUtils.add(getMoney+"","0",2);
        }

        public double getGetMoenyDouble(){
            try {
                return Double.parseDouble(getMoney);
            }catch (Exception e){
                e.printStackTrace();
                return 0;
            }
        }
    }

    public double getGrabMoney(){
        try {
            return Double.parseDouble(grabMoney);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;

    }
}
