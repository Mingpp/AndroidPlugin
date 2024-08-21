package com.example.myapplication.intercept;

public class MyClassLoader extends ClassLoader {

    /**
     * 将字节码转换为 Class 对象
     *
     * @param className 类名
     * @param classData 字节码数据
     * @return 定义的 Class 对象
     */
    public Class<?> defineClass(String className, byte[] classData) {
        return defineClass(className, classData, 0, classData.length);
    }

}
