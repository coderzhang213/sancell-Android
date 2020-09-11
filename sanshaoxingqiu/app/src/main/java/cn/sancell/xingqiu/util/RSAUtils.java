package cn.sancell.xingqiu.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;

import cn.sancell.xingqiu.BuildConfig;


public final class RSAUtils {

    //测试
    private static final String RSA_PUBLICE_TEST =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEbBYUypDl3CkNhGPjRuvWbwcj\n" +
                    "07ObwT0heTlmtRl708+UB4gC7SzUeL7AbuapajfYdq7CeSZm4AMoVXl/f69KkdDO\n" +
                    "yFugKSic9i7MvJXchwvwWlYOTQkJ8sr3rKZGyPhRwmCKupQ4h/8uvmNpV5tGf+AR\n" +
                    "zj2f/KAn/z+QLSr4pwIDAQAB";
    //正式
    private static final String RSA_PUBLICE_Off =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApSHKkWF3tnHcv8SzMU0Yb3in/p8IGmbndAiHJi1EvyhfSCEUSwc41982cYpdo4uH91JAOY9Vb2XBoa0LdvEsfwdvq8gzx/GBCS4vSoxMNWrlcb3KxXhkYiOXjjJvK2O3plvZj0Y18BeYZIzKRABb92uZEmh/UaqG78L49vov3YSrwOjRwS1XRI+LqT2pChixkX6XX2XdpkdnP8LkTfclvymI6nZavGNedj4crjsBYLMeCTZl0LTRr6GfLZUD2xCRJavVH1uaFhJIGdd9mLdViVFCO84N/Qii8JkVk7zCiuLjpBV/Cc2KISsQ/jDIDAmr7OaxZ/Kt3imGEzZtTZKBxQIDAQAB";

    private static String RSA_PUBLICE = BuildConfig.IS_OFFICIAL ? RSA_PUBLICE_Off : RSA_PUBLICE_TEST;

//    static {
//        switch (BuildConfig.ENVIRONMENT) {
//            case "test":
//                RSA_PUBLICE = RSA_PUBLICE_TEST;
//                break;
//            case "rdc":
//                RSA_PUBLICE = RSA_PUBLICE_TEST;
//                break;
//            case "pre":
//                RSA_PUBLICE = RSA_PUBLICE_TEST;
//                break;
//            case "rp":
//                RSA_PUBLICE = RSA_PUBLICE_TEST;
//                break;
//            case "pre_re":
//                RSA_PUBLICE = RSA_PUBLICE_TEST;
//                break;
//            default:
//                RSA_PUBLICE = RSA_PUBLICE_Off;
//                break;
//        }
//    }

    private static final String ALGORITHM = "RSA";
    /** */
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;


    /**
     * 得到公钥
     *
     * @param algorithm
     * @param bysKey
     * @return
     */
    private static PublicKey getPublicKeyFromX509(String algorithm,
                                                  String bysKey) throws NoSuchAlgorithmException, Exception {
        byte[] decodedKey = Base64.decode(bysKey, Base64.DEFAULT);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(x509);
    }

    private static RSAPublicKey getPublicKeyFromX509_other(String algorithm,
                                                           String bysKey) throws Exception {
        byte[] decodedKey = Base64.decode(bysKey, Base64.DEFAULT);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        RSAPublicKey publicKey = null;
        try {
            publicKey = (RSAPublicKey) keyFactory.generatePublic(x509);
        } catch (InvalidKeySpecException var4) {

        }
        return publicKey;
    }


    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    /**
     * 使用公钥加密
     *
     * @param content
     * @return
     */
    public static String encryptByPublic(String content) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLICE);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);
            byte plaintext[] = content.getBytes("UTF-8");
            byte[] output = cipher.doFinal(plaintext);
            String s = new String(Base64.encode(output, Base64.DEFAULT));
            return s;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 使用公钥加密
     *
     * @param map
     * @return
     */
    public static String encryptByPublic(Map map) {
        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            //升序排序
            public int compare(Map.Entry<String, String> o1,
                               Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        String content = "";
        for (Map.Entry<String, String> mapping : list) {
            content += mapping.getValue();
        }
        try {
            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLICE);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);
            byte[] inputArray = content.getBytes("UTF-8");
            int inputLength = inputArray.length;
            System.out.println("加密字节数：" + inputLength);
            // 最大加密字节数，超出最大字节数需要分组加密
            int MAX_ENCRYPT_BLOCK = 117;
            // 标识
            int offSet = 0;
            byte[] resultBytes = {};
            byte[] cache = {};
            while (inputLength - offSet > 0) {
                if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
                    offSet += MAX_ENCRYPT_BLOCK;
                } else {
                    cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
                    offSet = inputLength;
                }
                resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
                System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
            }
            String s = new String(Base64.encode(resultBytes, Base64.DEFAULT));

            return s;
        } catch (Exception e) {
            return null;
        }
    }


    public static String encryptByPublic_other(String content) {
        try {
            RSAPublicKey publicKey = getPublicKeyFromX509_other(ALGORITHM, RSA_PUBLICE);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 模长
            //int key_len = publicKey.getModulus().bitLength() / 8;
            int key_len = 128;
            // 加密数据长度 <= 模长-11
            String iosDatas = new String(content.getBytes(), "UTF-8");
            String[] datas = splitString(iosDatas, key_len);
            String mi = "";
            //如果明文长度大于模长-11则要分组加密
            for (String s : datas) {
                byte plaintext[] = s.getBytes("UTF-8");
                byte[] output = cipher.doFinal(plaintext);
                mi += new String(Base64.encode(output, Base64.DEFAULT));
            }
            return mi;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 使用公钥解密
     *
     * @param content 密文
     * @return 解密后的字符串
     */
    public static String decryptByPublic(String content) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLICE);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, pubkey);
            byte[] encryptedData = Base64.decode(content, Base64.DEFAULT);
            byte[] decryptedData = cipher.doFinal(encryptedData);
            return new String(decryptedData);
        } catch (Exception e) {
            return null;
        }
    }

    public static RSAPrivateKey getRSAPrivateKeyBybase64(String base64s) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(base64s, Base64.DEFAULT));
        RSAPrivateKey privateKey = null;
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        try {
            privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException var4) {

        }

        return privateKey;
    }

    /**
     * ASCII码转BCD码
     */
    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    public static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }

    /**
     * BCD转字符串
     */
    public static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    /**
     * 拆分字符串
     */
    public static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i = 0; i < x + z; i++) {
            if (i == x + z - 1 && y != 0) {
                str = string.substring(i * len, i * len + y);
            } else {
                str = string.substring(i * len, i * len + len);
            }
            strings[i] = str;
        }
        return strings;
    }

    /**
     * 拆分数组
     */
    public static byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }

    /**
     * 生成32位随机字符串Skey
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        //1.  定义一个字符串（A-Z，a-z，0-9）即62个数字字母；
        String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //2.  由Random生成随机数
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        //3.  长度为几就循环几次
        for (int i = 0; i < length; ++i) {
            //从62个的数字或字母中选择
            int number = random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }

}
