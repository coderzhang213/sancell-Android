package cn.sancell.xingqiu.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sancell.xingqiu.SCApp;


/**
 * @author Alan_Xiong
 * @desc: 身份证验证工具
 * @time 2020-02-12 15:48
 */
public class IDCardUtil {

    public static class Result {
        private String error;

        public boolean isLegal() {
            //两个变量为默认值,即认为是合法的
            return error == null || error.equals("");
        }

        public String getError() {
            return error;
        }

        public void setError(String message) {
            this.error = message;
        }

        public void show(Context context) {
            if (!isLegal())
                SCApp.getInstance().showSystemCenterToast(error);
//                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        }

        public void show(String desc){
            if (!isLegal()){
                SCApp.getInstance().showSystemCenterToast(desc);
            }
        }

    }

    private final static int[] FACTOR = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8,
            4, 2};

    public static Result validateIDNum(String idNum) {
        Result result = new Result();// 记录验证结果
        //判断身份证是否为空
        if (TextUtils.isEmpty(idNum)) {
            result.setError("身份证号码不能为空");
            return result;
        }
        //身份证号码的长度只能为15位或18位
        int idNumLength = idNum.length();
        if (idNumLength != 15 && idNumLength != 18) {
            result.setError("身份证号码应该为15位或18位");
            return result;
        }
        //对身份证的字符做判断
        if (!isAllNum(idNum)) {
            result.setError(idNum.length() == 18 ? "18位号码除最后一位外,都应为数字" : "15位号码都应为数字");
            return result;
        }

        if (idNum.contains("x")) {
            result.setError("身份证x必须为大写");
            return result;
        }

        //判断地区编码
        Hashtable<String, String> h = getAreaCode();
        if (h.get(idNum.substring(0, 2)) == null) {
            result.setError("身份证地区编码错误");
            return result;
        }

        // 出生年月是否有效
        String idNum17;
        if (idNum.length() == 18) {
            idNum17 = idNum.substring(0, 17);
        } else {
            //如果是15为身份证则加上出生年代:19
            idNum17 = idNum.substring(0, 6) + "19" + idNum.substring(6, 15);
        }
        String strYear = idNum17.substring(6, 10);// 年份
        String strMonth = idNum17.substring(10, 12);// 月份
        String strDay = idNum17.substring(12, 14);// 月份
        if (!validateDate(strYear + "-" + strMonth + "-" + strDay)) {
            result.setError("身份证生日无效");
            return result;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime()
                    - format.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                result.setError("身份证生日不在有效范围");
                return result;
            }
        } catch (NumberFormatException | java.text.ParseException e) {
            e.printStackTrace();
        }


        //18位的身份证对最后一位校验码进行验证
        if (idNum.length() == 18 && !isCorrectID(idNum)) {
            result.setError("身份证无效,不是合法的身份证号码");
            return result;
        }

        return result;
    }


    /**
     * 功能：设置地区编码
     */
    private static Hashtable<String, String> getAreaCode() {
        Hashtable<String, String> hashTable = new Hashtable<>();
        hashTable.put("11", "北京");
        hashTable.put("12", "天津");
        hashTable.put("13", "河北");
        hashTable.put("14", "山西");
        hashTable.put("15", "内蒙古");
        hashTable.put("21", "辽宁");
        hashTable.put("22", "吉林");
        hashTable.put("23", "黑龙江");
        hashTable.put("31", "上海");
        hashTable.put("32", "江苏");
        hashTable.put("33", "浙江");
        hashTable.put("34", "安徽");
        hashTable.put("35", "福建");
        hashTable.put("36", "江西");
        hashTable.put("37", "山东");
        hashTable.put("41", "河南");
        hashTable.put("42", "湖北");
        hashTable.put("43", "湖南");
        hashTable.put("44", "广东");
        hashTable.put("45", "广西");
        hashTable.put("46", "海南");
        hashTable.put("50", "重庆");
        hashTable.put("51", "四川");
        hashTable.put("52", "贵州");
        hashTable.put("53", "云南");
        hashTable.put("54", "西藏");
        hashTable.put("61", "陕西");
        hashTable.put("62", "甘肃");
        hashTable.put("63", "青海");
        hashTable.put("64", "宁夏");
        hashTable.put("65", "新疆");
        hashTable.put("71", "台湾");
        hashTable.put("81", "香港");
        hashTable.put("82", "澳门");
        hashTable.put("91", "国外");
        return hashTable;
    }

    /**
     * 判断身份证字符是否合法
     */
    private static boolean isAllNum(String idNum) {
        String match = idNum.length() == 18 ? "^[0-9]{17}([0-9]|X)$" : "^[0-9]{15}$";
        Pattern pattern = Pattern.compile(match);
        Matcher isNum = pattern.matcher(idNum);
        return isNum.matches();
    }

    /**
     * 功能：判断字符串是否为日期格式
     */
    private static boolean validateDate(String strDate) {
        Pattern pattern = Pattern.compile(
                "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        return m.matches();
    }

    /**
     * 判断输入的身份证是否合法
     *
     * @param idNum 18位的身份证号
     * @return 合法true，反之false
     */
    private static boolean isCorrectID(String idNum) {
        boolean flag = false;
        if (idNum == null || idNum.trim().length() != 18) {
            return false;
        }
        String last = getLastNumOfID(idNum.substring(0, idNum.length() - 1));
        if (last.equals(String.valueOf(idNum.charAt(idNum.length() - 1)))) {
            flag = true;
        }
        return flag;
    }

    /**
     * 根据前17位身份证号，算出第18位数字
     *
     * @param id17 前17位身份证号
     * @return 第18位身份证号对应的数字
     */
    private static String getLastNumOfID(String id17) {
        int sum = sumFactor(id17);
        String res;
        if (sum == -1) {
            res = "输入的身份证为空";
        } else if (sum == -3) {
            res = "输入的身份证号码不为17位";
        } else {
            int mod = sum % 11;
            int last = (12 - mod) % 11;
            if (last == 10) {
                res = "X";//X代表罗马数字10
            } else {
                res = String.valueOf(last);
            }
        }
        return res;

    }

    /**
     * 计算前17位身份证号乘以各个数的权重的总和
     *
     * @param id17 前17位身份证号
     * @return 权重的总和
     */
    private static int sumFactor(String id17) {
        if (id17 == null || id17.trim().equals("")) {
            return -1; //输入的身份证为空
        }
        int len = id17.length();
        if (len != 17) {
            return -3; //输入的身份证号码不为17位
        }
        int sum = 0;
        for (int i = 0; i < len; i++) {
            sum += FACTOR[i] * Integer.parseInt(String.valueOf(id17.charAt(i)));
        }
        return sum;
    }
}
