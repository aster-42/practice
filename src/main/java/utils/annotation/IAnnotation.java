package utils.annotation;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class IAnnotation {

    /**
     * class annotation
     * */
    public static String name(Class<?> clazz) {
        if (clazz.isAnnotationPresent(ClassAnnotation.class)) {
            ClassAnnotation pageClass = clazz.getAnnotation(ClassAnnotation.class);
            return pageClass.name();
        }
        return null;
    }

    /**
     * class instance method annotation
     * */
    public static Map<String, Integer> methodNumber(Class<?> clazz) {
        Map<String, Integer> rs = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(FieldAnnotation.class)) {
                String name = f.getName();
                int number = f.getAnnotation(FieldAnnotation.class).number();
                rs.put(name, number);
                try {
                    PropertyDescriptor descriptor = new PropertyDescriptor(f.getName(), clazz);
                    // getter
                    Method getter = descriptor.getReadMethod();
                    Object o = getter.invoke(clazz);
                    System.out.println("getter: " + o);
                    // setter
                    Method setter = descriptor.getWriteMethod();
                    setter.invoke(clazz, 1024L);
                } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return rs;
    }

}
