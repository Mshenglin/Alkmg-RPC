package com.xu.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取单例对象的工厂
 * @author mashenglin
 */
public class SingletonFactory {
    private static final Map<String, Object> OBJECT_MAP = new HashMap<>();

    private SingletonFactory() {
    }

    /**
     * @param c   该类的class对象
     * @param <T> 泛型通配符
     * @return 传入的class对象的实例对象
     */
    public static <T> T getInstance(Class<T> c) {
        String key = c.toString();
        Object instance;
        //使用双重检验锁和反射获取对象
        synchronized (SingletonFactory.class) {
            instance = OBJECT_MAP.get(key);
            if (instance == null) {
                try {
                    instance = c.getDeclaredConstructor().newInstance();
                    OBJECT_MAP.put(key, instance);
                } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
        //将对象强制转换
        return c.cast(instance);
    }
    }

