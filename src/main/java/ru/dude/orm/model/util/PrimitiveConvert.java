package ru.dude.orm.model.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Конвертер примитивов
 *
 * @author dude.
 */
public class PrimitiveConvert {

    private final static Map<String, Class<?>> map = new HashMap<>();

    static {
        map.put("boolean", Boolean.class);
        map.put("byte", Byte.class);
        map.put("short", Short.class);
        map.put("char", Character.class);
        map.put("int", Integer.class);
        map.put("long", Long.class);
        map.put("float", Float.class);
        map.put("double", Double.class);
    }

    public static String getClassName(String primitiveName) {
        if (map.containsKey(primitiveName)) {
            return map.get(primitiveName).getName();
        }
        return "";
    }

    public static boolean isPrimitive(String primitiveName) {
        return map.containsKey(primitiveName);
    }

}
