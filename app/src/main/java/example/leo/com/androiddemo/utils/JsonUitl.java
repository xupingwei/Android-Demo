package example.leo.com.androiddemo.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: AndroidDemo
 * @Package: example.leo.com.androiddemo.utils
 * @ClassName: JsonUitl
 * @Description: Gson工具类
 * @Author: wanglintao
 * @CreateDate: 2019/1/23 15:46
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/23 15:46
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class JsonUitl {
    private static Gson mGson = new Gson();

    /**
     * 将json字符串转化成实体对象
     *
     * @param json
     * @param classOfT
     * @return
     */
    public static Object stringToObject(String json, Class classOfT) {
        return mGson.fromJson(json, classOfT);
    }

    /**
     * 将对象准换为json字符串 或者 把list 转化成json
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String objectToString(T object) {
        return mGson.toJson(object);
    }

    /**
     * 把json 字符串转化成list
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> stringToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }
}
