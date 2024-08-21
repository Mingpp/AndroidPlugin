package com.example.myapplication.intercept;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


public class CustomInterceptorGenerator implements Opcodes {

    public static byte[] generate() {
        ClassWriter cw = new ClassWriter(0);

        // 定义类
        cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER, "CustomInterceptor", null, "java/lang/Object", new String[]{"okhttp3/Interceptor"});

        // 定义默认构造函数
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        // 定义 intercept 方法
        mv = cw.visitMethod(ACC_PUBLIC, "intercept", "(Lokhttp3/Interceptor$Chain;)Lokhttp3/Response;", null, new String[]{"java/io/IOException"});
        mv.visitCode();

        // Request originalRequest = chain.request();
        mv.visitVarInsn(ALOAD, 1); // 加载 chain
        mv.visitMethodInsn(INVOKEINTERFACE, "okhttp3/Interceptor$Chain", "request", "()Lokhttp3/Request;", true); // 调用 chain.request()
        mv.visitVarInsn(ASTORE, 2); // 将返回的 Request 存储在局部变量2

        // Request modifiedRequest = originalRequest.newBuilder()
        mv.visitVarInsn(ALOAD, 2); // 加载 originalRequest
        mv.visitMethodInsn(INVOKEVIRTUAL, "okhttp3/Request", "newBuilder", "()Lokhttp3/Request$Builder;", false); // 调用 newBuilder()

        // .header("Authorization", "Bearer your_token")
        mv.visitLdcInsn("Authorization");
        mv.visitLdcInsn("Bearer your_token");
        mv.visitMethodInsn(INVOKEVIRTUAL, "okhttp3/Request$Builder", "header", "(Ljava/lang/String;Ljava/lang/String;)Lokhttp3/Request$Builder;", false); // 调用 header

        // .build();
        mv.visitMethodInsn(INVOKEVIRTUAL, "okhttp3/Request$Builder", "build", "()Lokhttp3/Request;", false); // 调用 build()
        mv.visitVarInsn(ASTORE, 3); // 将返回的 modifiedRequest 存储在局部变量3

        // return chain.proceed(modifiedRequest);
        mv.visitVarInsn(ALOAD, 1); // 加载 chain
        mv.visitVarInsn(ALOAD, 3); // 加载 modifiedRequest
        mv.visitMethodInsn(INVOKEINTERFACE, "okhttp3/Interceptor$Chain", "proceed", "(Lokhttp3/Request;)Lokhttp3/Response;", true); // 调用 proceed()
        mv.visitInsn(ARETURN); // 返回 Response

        mv.visitMaxs(3, 4);
        mv.visitEnd();

        cw.visitEnd();

        return cw.toByteArray();
    }
}