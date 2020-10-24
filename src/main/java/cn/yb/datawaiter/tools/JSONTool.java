package cn.yb.datawaiter.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class JSONTool {
    public static <T> T toObject(Map<String, Object> properties, Class<T> tClass) {
        T t = ReflectTool.getInstanceOfT(tClass);
        Map<String, Method> methodMap = ReflectTool.getMethod(ReflectTool.MethodNameEnum.set, tClass);
        for (String name : methodMap.keySet()) {
            Object obj = properties.get(name.replace("set", ""));
            if (obj != null) {
                try {
                    if (methodMap.get(name).getGenericParameterTypes()[0] == obj.getClass()) {
                        methodMap.get(name).invoke(t, obj);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        }
        return t;
    }
}
