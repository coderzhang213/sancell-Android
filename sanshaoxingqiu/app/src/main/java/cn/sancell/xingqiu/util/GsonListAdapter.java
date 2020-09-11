package cn.sancell.xingqiu.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class GsonListAdapter implements JsonDeserializer<JsonArray> {
    @Override
    public JsonArray deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            if (json.isJsonArray()){
                return json.getAsJsonArray();
            }else{
                return new JsonArray();
            }
        }catch (Exception e){

        }
        return null;
    }
}
