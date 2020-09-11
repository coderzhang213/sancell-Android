package cn.sancell.xingqiu.order.entity.res;

public class OrderRes {

    /**
     * grouponNo :
     * grouponChildNo : GC202001035110250575
     * addressInfo : {"id":205,"userId":14412,"consignee":"熊出没风波里","isDefault":2,"streetId":310107021,"provinceId":31,"cityId":3101,"areasId":310107,"address":"天山西路101弄","zipCode":null,"email":null,"mobile":"15869325245","markName":"","lastChoose":1,"codeString":"上海市-市辖区-普陀区-万里街道","provinceName":"上海市","cityName":"市辖区","areasName":"普陀区","streetName":"万里街道"}
     * pointE2 : 89847768
     */

    public String grouponNo;
    public String grouponChildNo;
    public AddressInfoBean addressInfo;
    public int pointE2;

    public static class AddressInfoBean {
        /**
         * id : 205
         * userId : 14412
         * consignee : 熊出没风波里
         * isDefault : 2
         * streetId : 310107021
         * provinceId : 31
         * cityId : 3101
         * areasId : 310107
         * address : 天山西路101弄
         * zipCode : null
         * email : null
         * mobile : 15869325245
         * markName :
         * lastChoose : 1
         * codeString : 上海市-市辖区-普陀区-万里街道
         * provinceName : 上海市
         * cityName : 市辖区
         * areasName : 普陀区
         * streetName : 万里街道
         */
        public int id;
        public int userId;
        public String consignee;
        public int isDefault;
        public int streetId;
        public int provinceId;
        public int cityId;
        public int areasId;
        public String address;
        public Object zipCode;
        public Object email;
        public String mobile;
        public String markName;
        public int lastChoose;
        public String codeString;
        public String provinceName;
        public String cityName;
        public String areasName;
        public String streetName;
    }
}
