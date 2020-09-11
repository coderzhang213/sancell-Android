package cn.sancell.xingqiu.live.bean;

/**
 * Created by zj on 2019/11/28.
 * 直播间群组列表
 */
public class GroupInfo {
    private String groupName;
    private String icon;
    private String id;
    private String inGroup;
    private String intro;
    private String tid;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInGroup() {
        return inGroup;
    }

    public void setInGroup(String inGroup) {
        this.inGroup = inGroup;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
}
