package cn.yb.datawaiter.tools;



import cn.yb.datawaiter.exception.CommonException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectTool {

    /**
     * @param methodName map 的主键值
     * @param list
     * @return
     */
    public static <T1, T2> Map<T1, List<T2>> getListIDMap(String methodName, List<T2> list) {
        Map<T1, List<T2>> map = new HashMap<>();
        if (Tool.isEmpty(list)) {
            return map;
        }
        List<T2> ts;
        Class tClass = list.get(0).getClass();
        Method m = null;
        try {
            m = tClass.getMethod(methodName);
            for (T2 t : list
            ) {
                Object key = m.invoke(t);
                if (key != null) {
                    ts = map.get(key);
                    if (ts == null) {
                        ts = new ArrayList<>();
                        ts.add(t);
                        map.put((T1) key, ts);
                    } else {
                        ts.add(t);
                    }
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    public static <T, T2> Map<T, T2> getIDMap(String methodName, List<T2> list) {
        Map<T, T2> map = new HashMap<>();
        if (Tool.isEmpty(list)) {
            return map;
        }
        Class clazz = list.get(0).getClass();
        Method m = null;

        //Object[] paramters =new Object[1];
        try {
            m = clazz.getMethod(methodName);
            for (T2 t : list
            ) {
                T key = (T) m.invoke(t);
                if (map.containsKey(key)) {
                    throw new CommonException("主键重复：" + key);
                } else {
                    map.put(key, t);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return map;
    }

    /**
     * 老对象字段替换 成新对象的字段  只改基本对象
     *
     * @param oldT
     * @param newT
     * @param methodCustom
     */
    public static <T> void replaceFiled(T oldT, T newT, MethodCustom methodCustom) {
        Map<String, Method> getMethodName = methodCustom.getMethodMap;
        for (Method setM : methodCustom.setMehods) {
            try {
                Object obj = getMethodName.get(setM.getName().replace("set", "get")).invoke(newT, null);
                String name = setM.getParameterTypes()[0].getName();
                switch (name) {
                    case "float":
                    case "int":
                    case "boolean":
                    case "java.lang.Double":
                    case "java.lang.Long":
                    case "java.lang.Integer":
                    case "java.lang.Float":
                    case "java.lang.String":
                    case "java.lang.Boolean":
                        setM.invoke(oldT, obj);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

   public enum MethodNameEnum {
        set,
        get,
    }

    private static ReflectTool reflectTool;

    public static ReflectTool getInstance() {
        if (reflectTool == null) {
            reflectTool = new ReflectTool();
        }
        return reflectTool;
    }

    /* */
    /**
     * @param methodName map 的主键值 ，T对象的 methodName 不允许重复
     * @param list
     * @param <T>
     * @return
     *//*
    public static <T> Map<String, T> getIDMap(String methodName, List<T> list) throws ZJDException {
        Class clazz=null;
        if (!Tool.isEmpty(list)) {
            clazz = list.get(0).getClass();
        }
        Map<String, T> map = getIDMap(methodName, list,clazz);
        return map;
        *//*Map<String, T> map = new HashMap<>();

        Class tClass = list.get(0).getClass();
        Method m = null;
        //Object[] paramters =new Object[1];
        try {
            m = tClass.getMethod(methodName);
            for (T t : list
            ) {
                String key = m.invoke(t).toString();
                if (map.containsKey(key)) {
                    throw new ZJDException("主键重复：" + key);
                } else {
                    map.put(key, t);
                }

            }
        } catch (exception e) {
            throw new ZJDException(e.getMessage());
        }
        return map;*//*
    }*/

    private static Map<String, MethodCustom> methodCustomMap = new HashMap<>();

    /**
     * 包含 get方法 、 set方法集合
     */
    public static class MethodCustom {

        private static MethodCustom methodCustom = null;

        public static <T> MethodCustom getInstance(Class<T> tClass) {
            synchronized(ReflectTool.class) {
                //methodCustom = methodCustomMap.get(tClass.getName());
                methodCustom= null;
                if (methodCustom != null) {
                    return methodCustom;
                }
                methodCustom = new MethodCustom();
                methodCustomMap.put(tClass.getName(), methodCustom);
                methodCustom.getMethods = new ArrayList<>();
                methodCustom.setMehods = new ArrayList<>();
                methodCustom.getMethodMap = new HashMap<>();
                methodCustom.setetMethodMap = new HashMap<>();
                Method[] ms = tClass.getMethods();
                for (Method m :
                        ms) {
                    String name = m.getName();
                    if (name.equals("getDeclaringClass") || name.equals("setDeclaringClass") || name.equals("getClass") || name.equals("setClass")) {
                        continue;
                    }
                    if (name.startsWith("get")) {
                        methodCustom.getMethods.add(m);
                        methodCustom.getMethodMap.put(name, m);
                    } else if (name.startsWith("set")) {
                        methodCustom.setMehods.add(m);
                        methodCustom.setetMethodMap.put(name, m);
                    }
                }

                return methodCustom;
            }

        }

        private MethodCustom() {

        }

        private List<Method> getMethods;
        private List<Method> setMehods;
        private Map<String, Method> getMethodMap;
        private Map<String, Method> setetMethodMap;

        public List<Method> getGetMethods() {
            return getMethods;
        }

        public void setGetMethods(List<Method> getMethods) {
            this.getMethods = getMethods;
        }

        public List<Method> getSetMehods() {
            return setMehods;
        }

        public void setSetMehods(List<Method> setMehods) {

            this.setMehods = setMehods;
        }

        public Map<String, Method> getGetMethodMap() {
            return getMethodMap;
        }

        public void setGetMethodMap(Map<String, Method> getMethodMap) {
            this.getMethodMap = getMethodMap;
        }

        public Map<String, Method> getSetetMethodMap() {
            return setetMethodMap;
        }

        public void setSetetMethodMap(Map<String, Method> setetMethodMap) {
            this.setetMethodMap = setetMethodMap;
        }

        public MethodCustom getMethodCustom() {
            return methodCustom;
        }

        public void setMethodCustom(MethodCustom methodCustom) {
            MethodCustom.methodCustom = methodCustom;
        }
    }


    /**
     * 得到 泛型类的 方法名为主键 的方法
     *
     * @param methodNameEnum 只有 get set 方法
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> Map<String, Method> getMethod(MethodNameEnum methodNameEnum, Class<T> tClass) {

        //T t = getInstanceOfT(tClass);

        MethodCustom methodCustom = MethodCustom.getInstance(tClass);
        List<Method> methods = null;
        switch (methodNameEnum) {
            case get:
                methods = methodCustom.getGetMethods();

                break;
            case set:
                methods = methodCustom.getSetMehods();
                break;
        }
        if (methods != null) {
            return getMethodNameMap(methods);
        }
        return null;
    }

    /**
     * 得到  方法名为主键 的方法
     *
     * @param methods
     * @return
     */
    private static Map<String, Method> getMethodNameMap(List<Method> methods) {
        Map<String, Method> map = new HashMap<>();
        if (methods == null) {
            return null;
        }
        for (Method m : methods
        ) {
            map.put(m.getName(), m);

        }
        return map;
    }


    /**
     * 创建实例化对象
     *
     * @param <T>
     * @return
     */
    public static <T> T getInstanceOfT(Class<T> tClass) {
        try {
            return tClass.newInstance();
        } catch (Exception e) {
            // Oops, no default constructor
            throw new RuntimeException(e);
        }
    }


}
