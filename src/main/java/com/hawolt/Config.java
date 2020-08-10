package com.hawolt;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;

public class Config extends HashMap<String, String> {

    private static Config internal;

    public static Config internal() throws IOException {
        if (internal != null) return internal;
        try (InputStream stream = ClasspathUtil.getResourceAsStream("config.json")) {
            return internal = getConfig(stream);
        }
    }

    public static Config load(String resource) throws IOException {
        try (InputStream stream = ClasspathUtil.getResourceAsStream(resource)) {
            return getConfig(stream);
        }
    }

    private static Config getConfig(InputStream stream) throws IOException {
        Config config = new Config();
        String content = ClasspathUtil.readStream(stream);
        JSONObject object = new JSONObject(content);
        config.loadObject(object);
        return config;
    }

    public static Config load(File file) throws IOException {
        try (FileInputStream stream = new FileInputStream(file)) {
            return getConfig(stream);
        }
    }

    private void loadObject(JSONObject config, String... previous) {
        for (String key : config.keySet()) {
            Object object = config.get(key);
            if (object instanceof JSONObject) {
                loadObject((JSONObject) object, add(key, previous));
            } else if (object instanceof JSONArray) {
                loadArray(key, (JSONArray) object, add(key, previous));
            } else {
                put(nest(key, previous), object.toString());
            }
        }
    }

    private void loadArray(String key, JSONArray array, String... previous) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length(); i++) {
            Object object = array.get(i);
            if (object instanceof JSONObject) {
                loadObject((JSONObject) object, previous);
            } else if (object instanceof JSONArray) {
                loadArray(key, array, previous);
            } else {
                builder.append(object.toString()).append(",");
            }
        }
        if (builder.length() > 0) builder.setLength(builder.length() - 1);
        if (builder.length() > 0) put(nest(key, previous), builder.toString());
    }

    private String nest(String key, String[] previous) {
        StringBuilder temp = new StringBuilder();
        for (String object : previous) {
            temp.append(object).append('.');
        }
        return temp.append(key).toString();
    }

    private String[] add(String key, String... previous) {
        String[] temp = Arrays.copyOf(previous, previous.length + 1);
        temp[temp.length - 1] = key;
        return temp;
    }
}
