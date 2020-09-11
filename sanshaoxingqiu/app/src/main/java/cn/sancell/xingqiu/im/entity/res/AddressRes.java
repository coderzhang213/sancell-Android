package cn.sancell.xingqiu.im.entity.res;

import java.util.List;

/**
  * @author Alan_Xiong
  *
  * @desc: 我的群组返回
  * @time 2019-11-20 22:53
  */
public class AddressRes {

    public int dataCount;

    public List<TeamData> dataList;

    public static class TeamData {

        public int id;
        public String tid;
        public String groupName;
        public String icon;
        public String intro;


    }
}
