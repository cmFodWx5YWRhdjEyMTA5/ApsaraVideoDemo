package avd.nk.com.apsaravideodemo.widget;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectHelper {
    // Create

    public static <T> T create(Class<T> cls, Object... args) {
        return create(cls, resolveArgsTypes(args), args);
    }

    public static <T> T create(Class<T> cls, Class<?>[] types, Object... args) {
        try {
            Constructor<T> ctr = null;
            if (types == null || types.length == 0) {
                ctr = cls.getDeclaredConstructor();
            } else {
                ctr = cls.getDeclaredConstructor(types);
            }
            if (ctr != null) {
                ctr.setAccessible(true);
            }
            return ctr.newInstance(args);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    // Getter

    public static Method getMethod(Class<?> targetClass, String methodName, Class<?>... types) {
        try {
            Method method = targetClass.getDeclaredMethod(methodName, types);
            method.setAccessible(true);
            return method;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Field getField(Class<?> targetClass, String fieldName) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (Throwable e) {
            return null;
        }
    }

    public static Field findField(Object instance, String name) throws NoSuchFieldException{
        for (Class clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(name);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field;
            }
            catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        throw new NoSuchFieldException(new StringBuilder().append("Field ").append(name).append(" not found in ").append(instance.getClass()).toString());
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object target, String... fieldNames) {
        if (target == null || fieldNames.length == 0) {
            return null;
        }
        try {
            Field field;
            for (String fieldName : fieldNames) {
                field = findField(target, fieldName);
                if (field != null) {
                    return (T) field.get(target);
                }
            }
        } catch (Throwable ex) {
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getStaticFieldValue(Class<?> targetClass, String... fieldNames) {
        if (targetClass == null || fieldNames.length == 0) {
            return null;
        }
        try {
            Field field;
            for (String fieldName : fieldNames) {
                field = getField(targetClass, fieldName);
                if (field != null) {
                    return (T) field.get(null);
                }
            }
        } catch (Throwable ex) {
        }
        return null;
    }

    // Invoke

    public static Object invokeStatic(String className, String methodName, Object... args) {
        return invokeStatic(className, methodName, resolveArgsTypes(args), args);
    }

    public static Object invokeStatic(String className, String methodName, Class<?>[] argTypes, Object... args) {
        try {
            return invokeStatic(Class.forName(className), methodName, argTypes, args);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object invokeStatic(Class<?> classType, String methodName, Object... args) {
        return invokeStatic(classType, methodName, resolveArgsTypes(args), args);
    }

    public static Object invokeStatic(Class<?> classType, String methodName, Class<?>[] argTypes, Object... args) {
        try {
            return invoke(null, classType, methodName, argTypes, args);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object invokeStatic(Method method, Object... args) {
        return invoke(null, method, args);
    }

    /**
     * @param args
     *            (Note:there must be a clear distinction between int type and
     *            float, double, etc.)
     */
    public static Object invoke(Object obj, String methodName, Object... args) {
        return invoke(obj, obj.getClass(), methodName, resolveArgsTypes(args), args);
    }

    public static Object invoke(Object obj, Method method, Object... args) {
        try {
            return method.invoke(obj, args);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object invoke(Object obj, String methodName, Class<?>[] argTypes, Object... args) {
        return invoke(obj, obj.getClass(), methodName, argTypes, args);
    }

    public static Object invoke(Object obj, Class<?> targetClass, String methodName, Class<?>[] argTypes, Object... args) {
        try {
            Method method = targetClass.getDeclaredMethod(methodName, argTypes);
            method.setAccessible(true);
            return method.invoke(obj, args);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Construct a object by class name */
    public static Object construct(String className, Object... args) {
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return resolve primitive type for all primitive wrapper types.
     */
    public static Class<?> rawType(Class<?> type) {
        if (type.equals(Boolean.class)) {
            return boolean.class;
        } else if (type.equals(Integer.class)) {
            return int.class;
        } else if (type.equals(Float.class)) {
            return float.class;
        } else if (type.equals(Double.class)) {
            return double.class;
        } else if (type.equals(Short.class)) {
            return short.class;
        } else if (type.equals(Long.class)) {
            return long.class;
        } else if (type.equals(Byte.class)) {
            return byte.class;
        } else if (type.equals(Character.class)) {
            return char.class;
        }

        return type;
    }

    private static Class<?>[] resolveArgsTypes(Object... args) {
        Class<?>[] types = null;
        if (args != null && args.length > 0) {
            types = new Class<?>[args.length];
            for (int i = 0; i < args.length; ++i) {
                types[i] = rawType(args[i].getClass());
            }
        }
        return types;
    }

    /**
     * 通过反射设置对象的变量值
     * @param instance
     * @param name
     * @param value
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void setField(Object instance, String name, Object value) throws NoSuchFieldException, IllegalAccessException {
        findField(instance, name).set(instance, value);
    }
}
