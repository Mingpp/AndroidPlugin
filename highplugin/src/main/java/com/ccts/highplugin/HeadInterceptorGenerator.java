package com.ccts.highplugin;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;

public class HeadInterceptorGenerator implements Opcodes {

    public static byte[] generate() {
        ClassWriter cw = new ClassWriter(0);
        MethodVisitor mv;

        // 创建类定义
        cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER, "com/example/myapplication/intercept/HeadInterceptor", null, "java/lang/Object", new String[]{"okhttp3/Interceptor"});

        // 默认构造函数
        mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        // intercept 方法
        mv = cw.visitMethod(ACC_PUBLIC, "intercept", "(Lokhttp3/Interceptor$Chain;)Lokhttp3/Response;", null, new String[]{"java/io/IOException"});
        mv.visitCode();

        // okhttp3.Request original = chain.request();
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEINTERFACE, "okhttp3/Interceptor$Chain", "request", "()Lokhttp3/Request;", true);
        mv.visitVarInsn(ASTORE, 2);

        // okhttp3.Request.Builder requestBuilder = original.newBuilder()
        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, "okhttp3/Request", "newBuilder", "()Lokhttp3/Request$Builder;", false);
        mv.visitVarInsn(ASTORE, 3);

        // .header("X-b3-traceId", "7ac0c001xxxxxxxx");
        mv.visitVarInsn(ALOAD, 3);
        mv.visitLdcInsn("X-b3-traceId");
        mv.visitLdcInsn("7ac0c001xxxxxxxx");
        mv.visitMethodInsn(INVOKEVIRTUAL, "okhttp3/Request$Builder", "header", "(Ljava/lang/String;Ljava/lang/String;)Lokhttp3/Request$Builder;", false);
        mv.visitVarInsn(ASTORE, 3);

        // okhttp3.Request request = requestBuilder.build();
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(INVOKEVIRTUAL, "okhttp3/Request$Builder", "build", "()Lokhttp3/Request;", false);
        mv.visitVarInsn(ASTORE, 4);

        // System.out.println("add headers: " + request.headers());
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitLdcInsn("add headers: ");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitMethodInsn(INVOKEVIRTUAL, "okhttp3/Request", "headers", "()Lokhttp3/Headers;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

        // return chain.proceed(request);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitMethodInsn(INVOKEINTERFACE, "okhttp3/Interceptor$Chain", "proceed", "(Lokhttp3/Request;)Lokhttp3/Response;", true);
        mv.visitInsn(ARETURN);

        mv.visitMaxs(3, 5);
        mv.visitEnd();

        // 完成类定义
        cw.visitEnd();

        return cw.toByteArray();
    }

    public static void saveClassToFile(byte[] classData, String path) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(classData);
        }
    }


}

