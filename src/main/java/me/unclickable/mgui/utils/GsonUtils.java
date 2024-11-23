package me.unclickable.mgui.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GsonUtils {

    private static final Gson GSON = new Gson();

    public static <T> List<T> deserializeList(String json, Class<T> type) {
        return GSON.fromJson(json, TypeToken.getParameterized(List.class, type).getType());
    }

    public static <K, V> Map<K, V> deserializeMap(String json, Class<K> keyType, Class<V> valueType) {
        return GSON.fromJson(json, TypeToken.getParameterized(Map.class, keyType, valueType).getType());
    }

    public static String serialize(Object object) {
        return GSON.toJson(object);
    }

}
