package puzzle.lianche.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 反射工具
 * 
 * @author
 * @created 2012-3-20
 */
public class ReflectUtil {
    /**
     * 获取obj对象fieldName的Field
     * 
     * @param obj
     * @param fieldName
     * @return
     */
    public static Field getField(Object obj, String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
            }
        }
        return null;
    }

    /**
     * 获取obj对象fieldName的Field
     *
     * @param obj
     * @param methodName
     * @return
     */
    public static Method getMethod(Object obj, String methodName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getMethod(methodName);
            } catch (NoSuchMethodException e) {
            }
        }
        return null;
    }

    /**
     * 获取obj对象fieldName的属性值
     * 
     * @param obj
     * @param fieldName
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object getFieldValue(Object obj, String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = getField(obj, fieldName);
        Object value = null;
        if (field != null) {
            if (field.isAccessible()) {
                value = field.get(obj);
            } else {
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(false);
            }
        }
        return value;
    }

    /**
     * 设置obj对象fieldName的属性值
     * 
     * @param obj
     * @param fieldName
     * @param value
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setFieldValue(Object obj, String fieldName, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        if (field.isAccessible()) {
            field.set(obj, value);
        } else {
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        }
    }

    /**
     * 执行方法
     *
     * @param obj
     * @param methodName
     * @param args
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object invokeMethod(Object obj, String methodName, Object...args) throws Exception{
        Method method = getMethod(obj, methodName);
        if(method != null){
            return method.invoke(obj, args);
        }
        return null;
    }

    /**
     * 复制属性
     *
     * @param source
     * @param target
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void copyField(Object source, Object target){
        if(source == null || target == null)
            return;
        Field[] fields = null;
        try {
            fields = target.getClass().getDeclaredFields();
        }
        catch (Exception e){

        }
        if(fields != null && fields.length > 0) {
            Class sourceClazz = source.getClass();
            boolean sourceIsMap = false;
            if(sourceClazz.isAssignableFrom(Map.class) || (Map.class.isInstance(source)))
                sourceIsMap = true;
            for (Field targetField : fields) {
                try {
                    Object fieldValue = sourceIsMap ?
                            ((Map)source).get(targetField.getName()) :
                            sourceClazz.getField(targetField.getName()).get(source);
                    if (fieldValue != null) {
                        if(targetField.isAccessible()){
                            targetField.set(target, fieldValue);
                        }else{
                            targetField.setAccessible(true);
                            targetField.set(target, fieldValue);
                            targetField.setAccessible(false);
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }

    }
}
