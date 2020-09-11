package cn.sancell.xingqiu.order.entity.res;

import java.util.List;

public class PinOrderPackRes {

    public AddressInfo addressInfo;

    public InvoiceData invoiceData;

    public String remark;

    public Goods goods;

    public long orderCreateTime;

    public int payPlatform;

    public long orderPayTime;

    public long payAmtE2;

    public long payPointAmtE2;

    public long payMoneyAmtE2;

    public int pageStatus;

    public int isNeedInvoice;

    public String buyOrderId;

    public String grouponNo;



    public static class AddressInfo{

        public String consignee;

        public String mobile;

        public String codeString;

        public String markName;

    }

    public static class InvoiceData{
        public String id;

        public String orderId;

        public String orderParcelId;

        public int invoiceType;

        public int invoiceLookedUp;

        public String userId;

        public String email;

        public String companyName;

        public String companyIdentifyNumber;

        public String mobile;

        public String desc;

        public String makeOutInvoiceStatue;

        public String invoiceTypeStr;

        public String invoiceLookedUpStr;

        public String invoiceUrl;
    }

    public static class Goods{

        public String title;
        public int goodsId;

        public String num;

        public String specification;

        public String coverPic;

        public long grouponPriceE2;
    }
}
