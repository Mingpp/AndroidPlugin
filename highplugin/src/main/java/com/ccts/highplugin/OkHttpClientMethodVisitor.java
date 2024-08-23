package com.ccts.highplugin;


import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;


public class OkHttpClientMethodVisitor extends MethodVisitor {

    private boolean isInsideBuilderMethod = false;
    private boolean hasInjectedInterceptor = false;

    public OkHttpClientMethodVisitor(MethodVisitor methodVisitor) {
        super(Opcodes.ASM9, methodVisitor);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        // 查找类型指令 NEW
        super.visitTypeInsn(opcode, type);
        if (opcode == Opcodes.NEW && type.equals("okhttp3/OkHttpClient$Builder")) {
               isInsideBuilderMethod = true;
        }

    }


    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {


        // 在调用 OkHttpClient.Builder.build() 之前插入 addInterceptor
        if (opcode == Opcodes.INVOKEVIRTUAL && "okhttp3/OkHttpClient$Builder".equals(owner) && "build".equals(name) && "()Lokhttp3/OkHttpClient;".equals(descriptor)) {
            if(isInsideBuilderMethod && !hasInjectedInterceptor) {
               injectLoggingInterceptor();
                hasInjectedInterceptor = true;
            }
        }


        // 调用父类的方法,一定放在后边
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);

    }

    private void injectLoggingInterceptor() {

        mv.visitTypeInsn(Opcodes.NEW, "com/example/myapplication/intercept/HeadInterceptor");
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "com/example/myapplication/intercept/HeadInterceptor", "<init>", "()V", false);

        // 调用 addInterceptor 方法
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "okhttp3/OkHttpClient$Builder", "addInterceptor", "(Lokhttp3/Interceptor;)Lokhttp3/OkHttpClient$Builder;", false);
    }











//    // 内部类
//    public static class HeadInterceptor implements okhttp3.Interceptor {
//        @NotNull
//        @Override
//        public okhttp3.Response intercept(Chain chain) throws IOException {
//            okhttp3.Request original = chain.request();
//
//            // 添加自定义头信息
//            okhttp3.Request.Builder requestBuilder = original.newBuilder()
//                    .header("X-b3-traceId", "7ac0c001xxxxxxxx");
//
//            okhttp3.Request request = requestBuilder.build();
//
//            System.out.println("add headers: " + request.headers());
//
//            return chain.proceed(request);
//        }
//    }





}
