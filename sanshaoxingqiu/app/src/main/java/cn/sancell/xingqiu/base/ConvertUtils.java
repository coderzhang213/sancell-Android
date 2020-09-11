package cn.sancell.xingqiu.base;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.netease.nim.uikit.common.util.string.StringUtil;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.util.PreferencesUtils;
import cn.sancell.xingqiu.util.RSAUtils;
import cn.sancell.xingqiu.util.StringUtils;
import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * @author Alan_Xiong
 * @desc: 对象转化map
 * @time 2019-10-22 11:54
 */
public class ConvertUtils {

    public static HashMap<String, String> convertToMap(Object object) {
        if (object == null) {
            return null;
        }
        HashMap<String, String> arrayMap = new HashMap<>();
        Field[] fields = object.getClass().getFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                arrayMap.put(field.getName(), (String) field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return addToken(arrayMap);
    }

    /**
     * 添加加密的token
     *
     * @param maps
     * @return
     */
    private static HashMap<String, String> addToken(HashMap<String, String> maps) {
        maps.put("hashToken", RSAUtils.encryptByPublic(maps));
        return maps;
    }

    /**
     * 添加所有的参数
     *
     * @param par
     * @return
     */
    public static HashMap<String, String> getRequest(HashMap<String, String> par) {
        if (par == null) {
            par = new HashMap<>();
        }
        par.put("skey", PreferencesUtils.getString(Constants.Key.KEY_SKEY, ""));
        par.put("reqTime", StringUtils.getCurrentTime());
        par.put("hashToken", RSAUtils.encryptByPublic(par));
        return par;
    }

    /**
     * 把map转换成请求对象
     *
     * @param par
     * @return
     */
    public static RequestBody toHttpBuild(Map<String, String> par) {
        FormBody.Builder mBuilde = new FormBody.Builder();
        for (Map.Entry<String, String> entry : par.entrySet()) {
            mBuilde.add(entry.getKey(), entry.getValue());
        }
        return mBuilde.build();
    }

    /**
     * 添加所有的参数
     *
     * @return
     */
    public static HashMap<String, String> getRequest() {
        return getRequest(null);
    }

    /**
     * 只加密 time/key
     *
     * @param object
     * @return
     */
    public static HashMap<String, String> convertToMapPartRsa(Object object) {
        if (object == null) {
            return null;
        }
        HashMap<String, String> rsaMap = new HashMap<>();
        HashMap<String, String> arrayMap = new HashMap<>();
        Field[] fields = object.getClass().getFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                arrayMap.put(field.getName(), (String) field.get(object));
                if (TextUtils.equals("reqTime", field.getName()) || TextUtils.equals("skey", field.getName())) {
                    rsaMap.put(field.getName(), (String) field.get(object));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        arrayMap.put("hashToken", RSAUtils.encryptByPublic(rsaMap));
        return arrayMap;
    }

    public static HashMap<String, String> convertToMapPartRsa(Object object, @NonNull String... otherKey) {
        if (object == null) {
            return null;
        }
        HashMap<String, String> rsaMap = new HashMap<>();
        HashMap<String, String> arrayMap = new HashMap<>();
        Field[] fields = object.getClass().getFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                arrayMap.put(field.getName(), (String) field.get(object));
                if (TextUtils.equals("reqTime", field.getName()) || TextUtils.equals("skey", field.getName())) {
                    rsaMap.put(field.getName(), (String) field.get(object));
                }
                for (String str : otherKey) {
                    if (TextUtils.equals(str, field.getName())) {
                        rsaMap.put(field.getName(), (String) field.get(object));
                    }
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        arrayMap.put("hashToken", RSAUtils.encryptByPublic(rsaMap));
        return arrayMap;
    }

}
