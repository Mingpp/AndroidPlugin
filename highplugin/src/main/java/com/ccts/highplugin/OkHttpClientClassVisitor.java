package com.ccts.highplugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.IOException;

public class OkHttpClientClassVisitor  extends ClassVisitor {

    String path = "D:\\Android_Project\\MyApplication3\\app\\build\\intermediates\\javac\\debug\\compileDebugJavaWithJavac\\classes\\com\\example\\myapplication\\intercept\\";

    public OkHttpClientClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM9, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

        // 判断方法的返回类型是否是 OkHttpClient
        if (descriptor.endsWith("Lokhttp3/OkHttpClient;")) {
            byte[] head = HeadInterceptorGenerator.generate();
            try {
                HeadInterceptorGenerator.saveClassToFile(head,path + "HeadInterceptor.class");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new OkHttpClientMethodVisitor(mv);
        }

        return mv;
    }

}
