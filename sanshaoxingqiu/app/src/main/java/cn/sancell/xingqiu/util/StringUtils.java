package cn.sancell.xingqiu.util;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sancell.xingqiu.constant.Constants;

/**
 * Created by ai11 on 2019/5/6.
 */

public class StringUtils {
    private static final String TELREGEX = "^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\\d{8}$";
    private static final String TELPEGEX1 = "^1\\d{10}$";
    private static final String EMAILREGEX = "\\w+([-.]\\w+)*@\\w+([-]\\w+)*\\.(\\w+([-]\\w+)*\\.)*[a-z]{2,3}$";
    private static final String URLREGEX = "^(http://|https://)?((?:[A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)[/\\?\\:]?.*$";
    private static final String IPREGEX = "((?:(?:25[0-5]|2[0-4]\\\\d|[01]?\\\\d?\\\\d)\\\\.){3}(?:25[0-5]|2[0-4]\\\\d|[01]?\\\\d?\\\\d))";
    private static final String CHINESEREGEX = "[\\u4E00-\\u9FA5\\uF900-\\uFA2D]";
    private static final String IDNUMREGEX = "^\\d{8,18}|[0-9x]{8,18}|[0-9X]{8,18}?$";

    private StringUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    /**
     * 判断是否为空
     *
     * @param con
     * @return
     */
    public static boolean isTextEmpty(String con) {
        try {
            if (null == con) {
                return true;
            } else if (con.equals("")) {
                return true;
            } else if (con.equals("null")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Unicode转中文
     *
     * @param utfString
     * @return
     */
    public String convertUnicodeToChina(String utfString) {
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while ((i = utfString.indexOf("\\u", pos)) != -1) {
            sb.append(utfString.substring(pos, i));
            if (i + 5 < utfString.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(utfString.substring(i + 2, i + 6), 16));
            }
        }
        return sb.toString();
    }

    /**
     * 手机号码验证
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobile(String mobiles) {
        if (StringUtils.isTextEmpty(PreferencesUtils.getString(Constants.Key.KEY_mobileRule, ""))) {
            return check(mobiles, TELPEGEX1);
        } else {
            return check(mobiles, PreferencesUtils.getString(Constants.Key.KEY_mobileRule, ""));
        }
    }

    /**
     * 密码验证
     *
     * @param password
     * @return
     */
    public static boolean isPwd(String password) {
        if (StringUtils.isTextEmpty(PreferencesUtils.getString(Constants.Key.KEY_passwordRule, ""))) {
            return true;
        } else {
            return check(password, PreferencesUtils.getString(Constants.Key.KEY_passwordRule, ""));
        }
    }

    /**
     * 邮箱验证
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        return check(email, EMAILREGEX);
    }

    /**
     * http、https Url验证
     *
     * @param url
     * @return
     */
    public static boolean isHttpUrl(String url) {
        return check(url, URLREGEX);
    }

    /**
     * ip 验证
     *
     * @param ip
     * @return
     */
    public static boolean isIp(String ip) {
        return check(ip, IPREGEX);
    }


    /**
     * 中文验证
     *
     * @param sequence
     * @return
     */
    public static boolean isContainChinese(String sequence) {
        return check(sequence, CHINESEREGEX);
    }

    /**
     * 身份证号验证
     *
     * @param id
     * @return
     */
    public static boolean isIDNumber(String id) {
        return check(id, IDNUMREGEX);
    }

    private static boolean check(String content, String rule) {
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(content);
        return m.matches();
    }


    /**
     * 十六进制字符串转换为byte数组
     *
     * @param hexString
     * @return
     */
    public static byte[] hexString2Bytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (char2Byte(hexChars[pos]) << 4 | char2Byte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte char2Byte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     * byte数组转换为十六进制字符串
     *
     * @param b
     * @return
     */
    public static String bytes2HexString(byte[] b) {
        if (b.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < b.length; i++) {
            int value = b[i] & 0xFF;
            String hv = Integer.toHexString(value);
            if (hv.length() < 2) {
                sb.append(0);
            }

            sb.append(hv);
        }
        return sb.toString();
    }

    /**
     * int转换为byte数组
     *
     * @param res
     * @return
     */
    public static byte[] int2Byte(int res) {
        byte[] targets = new byte[4];
        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
        return targets;
    }

    /**
     * byte数组转换为int
     *
     * @param res
     * @return
     */
    public static int byte2Int(byte[] res) {
        // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
        int targets = (res[3] & 0xff) | ((res[2] << 8) & 0xff00) | ((res[1] << 16) & 0xff0000) | ((res[0] << 24) & 0xff000000);
        return targets;
    }

    /**
     * 获取时间戳
     */
    public static String getCurrentTime() {
        return (int) (System.currentTimeMillis() / 1000) + "";
    }

    /**
     * 保留几位小数
     */
    public static String saveDecimals(int cnt, Float value) {
        if (cnt == 2)
            return String.format("%.02f", value);
        else if (cnt == 1)
            return String.format("%.01f", value);
        else
            return String.format("%.0f", value);
    }

    public static int getShowStatus(int orderState, int payState, int isEvaluation, int deliveryState) {
        int status = 0;
        if (orderState == 1) {
            if (payState == 1) { //未支付
                status = Constants.OrderShowStatus.KEY_nopay;
            } else {
                if (deliveryState == 1) {  //待发货
                    status = Constants.OrderShowStatus.KEY_undelivered;
                } else if (deliveryState == 2) {   //已发货
                    status = Constants.OrderShowStatus.KEY_delivered;
                } else if (deliveryState == 3) {  //已收货
                    if (isEvaluation == 1) {  //已全部评价完
                        status = Constants.OrderShowStatus.KEY_evaluated;
                    } else if (isEvaluation == 2) {
                        status = Constants.OrderShowStatus.KEY_RECEIVE;
                    }
                }
            }
        } else if (orderState == 2 || orderState == 3 || orderState == 7 || orderState == 8) {  //订单关闭（可能是未支付失效 用户取消）
            status = Constants.OrderShowStatus.KEY_closed;
        } else if (orderState == 6) {  //处理中
            status = Constants.OrderShowStatus.KEY_processing;
        }
        return status;
    }

    public static int getShowStatusInOrder(int orderState, int payState, int isEvaluation, int deliveryState) {
        int status = 0;
        if (orderState == 1) {
            if (payState == 1) { //未支付
                status = Constants.OrderShowStatus.KEY_nopay;
            } else {
                if (deliveryState == 1) {  //待发货
                    status = Constants.OrderShowStatus.KEY_undelivered;
                } else if (deliveryState == 2) {   //已发货
                    status = Constants.OrderShowStatus.KEY_delivered;
                } else if (deliveryState == 3) {  //已收货
                    if (isEvaluation == 1) {  //已全部评价完
                        status = Constants.OrderShowStatus.KEY_evaluated;
                    } else if (isEvaluation == 2) {
                        status = Constants.OrderShowStatus.KEY_finished;
                    }
                }
            }
        } else if (orderState == 2 || orderState == 3 || orderState == 7 || orderState == 8) {  //订单关闭（可能是未支付失效 用户取消）
            status = Constants.OrderShowStatus.KEY_closed;
        } else if (orderState == 6) {  //处理中
            status = Constants.OrderShowStatus.KEY_processing;
        }
        return status;
    }

    /**
     * 获取保留两位小数
     *
     * @param price
     * @return
     */
    public static String getAllPrice(int price) {
        double num = (double) price / 100;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        return df.format(num);
    }

    /**
     * 获取保留两位小数
     *
     * @param price
     * @return
     */
    public static String getAllPrice(long price) {
        double num = (double) price / 100;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        return df.format(num);
    }


    /**
     * 获取小数点之前的数
     *
     * @param price
     * @return
     */
    public static String getPrice(int price) {
        double num = (double) price / 100;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String out_price = df.format(num);
        return out_price.substring(0, out_price.indexOf("."));
    }


    /**
     * 获取小数点之前的数
     *
     * @param price
     * @return
     */
    public static String getPrice(long price) {
        double num = (double) price / 100;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String out_price = df.format(num);
        return out_price.substring(0, out_price.indexOf("."));
    }


    /**
     * 获取小数点和小数点之后的两位数
     *
     * @param price
     * @return
     */
    public static String getPriceDecimal(int price) {
        double num = (double) price / 100;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String out_price = df.format(num);
        return out_price.substring(out_price.indexOf("."));
    }

    /**
     * 获取小数点和小数点之后的两位数
     *
     * @param price
     * @return
     */
    public static String getPriceDecimal(long price) {
        double num = (double) price / 100;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String out_price = df.format(num);
        return out_price.substring(out_price.indexOf("."));
    }

    /**
     * 返回text的长度，一个汉字算两个，数字和英文算一个
     *
     * @param text
     * @return
     */
    public static int judgeTextLength(String text) {
        if (TextUtils.isEmpty(text)) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            char item = text.charAt(i);
            if (item < 128) {
                count++;
            } else {
                count += 2;
            }
        }
        return count;
    }

    public static String getTitleDecimal(String title, int length) {
        if (title.length() > length) {
            title = title.substring(0, length) + "...";
        }
        return title;
    }

    /**
     * 整数(秒数)转换为时分秒数组
     *
     * @param time 秒数
     * @return 时分秒
     */
    public static String[] secToTimes(long time) {
        String[] timeStrs = new String[3];
        int hour;
        int minute;
        int second;
        if (time <= 0) {
            timeStrs[0] = "00";
            timeStrs[1] = "00";
            timeStrs[2] = "00";
            return timeStrs;
        } else {
            minute = (int) (time / 60);
            if (minute < 60) {
                second = (int) (time % 60);
                timeStrs[0] = "00";
                timeStrs[1] = unitFormat(minute);
                timeStrs[2] = unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99) {
                    timeStrs[0] = "99";
                    timeStrs[1] = "59";
                    timeStrs[2] = "59";
                    return timeStrs;
                }
                minute = minute % 60;
                second = (int) (time - hour * 3600 - minute * 60);
                timeStrs[0] = unitFormat(hour);
                timeStrs[1] = unitFormat(minute);
                timeStrs[2] = unitFormat(second);
            }
        }
        return timeStrs;
    }

    // 格式化事件规格
    private static String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

}
