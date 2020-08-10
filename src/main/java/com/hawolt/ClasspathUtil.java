package com.hawolt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ClasspathUtil {

    public static InputStream getResourceAsStream(String name) {
        return ClasspathUtil.class.getClassLoader().getResourceAsStream(name);
    }

    public static String readStream(InputStream stream) throws IOException {
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = stream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString("UTF-8");
        }
    }

    public static ByteArrayOutputStream readAsByte(InputStream stream) throws IOException {
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = stream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result;
        }
    }

    public static File getFile(String name) {
        return new File(name);
    }

    public static String readResource(String name) throws IOException {
        return readStream(getResourceAsStream(name));
    }

}
