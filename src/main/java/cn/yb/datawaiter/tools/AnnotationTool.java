package cn.yb.datawaiter.tools;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public  class AnnotationTool {
    /**
     * 获取实体类主键
     *
     * @param clazz
     * @return
     */
    public static Field getIdField(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Field item = null;
        for (Field field : fields) {
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                field.setAccessible(true);
                item = field;
                break;
            }
        }
        if (item == null) {
            Class superclass = clazz.getSuperclass();
            if (superclass != null) {
                item = getIdField(superclass);
            }
        }
        return item;
    }

    /**
     * 根据主键名称获取实体类主键属性值
     *
     * @param clazz
     * @param pkName
     * @return
     */
    public static Object getPkValueByName(Object clazz, String pkName) {
        try {
            String firstLetter = pkName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + pkName.substring(1);
            Method method = clazz.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(clazz, new Object[]{});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通过反射将 class1不为空的值赋值给class2
     *
     * @param class1
     * @param class2
     * @throws Exception
     */
    public static void reflectClass1ToClass2(Object class1, Object class2) throws Exception {
        Field[] field = class1.getClass().getDeclaredFields();
        for (int i = 0; i < field.length; i++) {
            String name = field[i].getName();
            if ("serialVersionUID".equals(name)) {
                continue;
            }
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            Method m1 = class1.getClass().getMethod("get" + name);
            Object value = m1.invoke(class1);
            if (value != null) {
                Field f = field[i];
                f.setAccessible(true);
                f.set(class2, value);
            }
        }
    }

    /**
     * 获取实体类 @Column 的其中一个属性名称
     *
     * @param clazz
     * @return
     */
    public static Map getColumnName(Class clazz) {
        Map map = new ConcurrentHashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                /**
                 * 获取字段名
                 */
                Column declaredAnnotation = field.getDeclaredAnnotation(Column.class);
                String column = declaredAnnotation.name();
                map.put("fieldNames", field.getName());
                map.put("column", column);
                break;
            }
        }
        return map;
    }

    /**
     * 通过获取类上的@Table注解获取表名称
     *
     * @param clazz
     * @return
     */
    public static String getTableName(Class clazz) {
        Annotation annotation = clazz.getAnnotation(Table.class);
        String name;
        if(annotation != null && annotation instanceof  Table){
            Table table = (Table) clazz.getAnnotation(Table.class);
            name = table.name();
        }else{
            name = clazz.getSimpleName().toLowerCase();
        }
        return name;
    }
}
