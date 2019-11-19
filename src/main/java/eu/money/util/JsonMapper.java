package eu.money.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class JsonMapper {

    private static final Gson GSON;

    static {
        GSON = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    private JsonMapper() {
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    public static String toJson(Object src) {
        return GSON.toJson(src);
    }
}
