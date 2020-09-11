package cn.sancell.xingqiu.bean;

/**
 * Created by zj on 2019/12/13.
 */
public class PurchaseInfo {
    private String title;
    private String conent;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getConent() {
        return conent;
    }

    public void setConent(String conent) {
        this.conent = conent;
    }
    public static PurchaseInfo getPurchaseInfo(String title,String conent){
        PurchaseInfo mPurchaseInfo=new PurchaseInfo();
        mPurchaseInfo.setTitle(title);
        mPurchaseInfo.setConent(conent);
        return mPurchaseInfo;
    }
}
