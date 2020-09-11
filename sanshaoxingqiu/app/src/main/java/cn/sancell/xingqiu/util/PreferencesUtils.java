package cn.sancell.xingqiu.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import cn.sancell.xingqiu.SCApp;
import cn.sancell.xingqiu.constant.Constants;
import cn.sancell.xingqiu.startup.bean.StartupDataBean;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ai11 on 2019/5/7.
 */

public class PreferencesUtils {


    public PreferencesUtils() { /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "sancell_data";

    /**
     * 存
     *
     * @param key   键
     * @param value 值
     */
    public static void put(@NonNull String key, @NonNull Object value) {
        put(SCApp.getContext(), key, value);
    }

    /**
     * 取
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return
     */
    public static Object get(String key, Object defaultValue) {
        return get(SCApp.getContext(), key, defaultValue);
    }

    public static String getString(String key, Object defaultValue) {
        return (String) get(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        SharedPreferences sp = SCApp.getContext().getSharedPreferences(FILE_NAME,
                MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }


    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences sp = SCApp.getContext().getSharedPreferences(FILE_NAME,
                MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, new Gson().toJson(object));
        }
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                MODE_PRIVATE);
        String value = sp.getString(key, String.valueOf(defaultObject));
        if (defaultObject instanceof String) {
            return value;
        } else if (defaultObject instanceof Integer) {
            return Integer.valueOf(value);
        } else if (defaultObject instanceof Boolean) {
            return Boolean.valueOf(value);
        } else if (defaultObject instanceof Float) {
            return Float.valueOf(value);
        } else if (defaultObject instanceof Long) {
            return Long.valueOf(value);
        }
        return new Gson().fromJson(value, defaultObject.getClass());
    }

    /**
     * 保存对象到sp文件中 被保存的对象须要实现 Serializable 接口
     *
     * @param key
     * @param value
     */
    public static void saveObject(String key, Object value) {
        put(key, value);
    }


    /**
     * desc:获取保存的Object对象
     *
     * @param key
     * @return modified:
     */
    public static <T> T readObject(String key, Class<T> clazz) {
        try {
            return (T) get(key, clazz.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取会员不同等级协议的list
     */
    public static List<StartupDataBean.MemberAgreementUrlData> readMemberAgreementListObject() {
        SharedPreferences sp = SCApp.getContext().getSharedPreferences(FILE_NAME,
                MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString(Constants.Key.KEY_memberAgreementUrlData, null);
        Type type = new TypeToken<List<StartupDataBean.MemberAgreementUrlData>>() {
        }.getType();
        List<StartupDataBean.MemberAgreementUrlData> arrayList = gson.fromJson(json, type);
        return arrayList;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method APPLY_METHOD = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (APPLY_METHOD != null) {
                    APPLY_METHOD.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }



}
