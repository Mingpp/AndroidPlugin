package com.example.myapplication.asmIntercept;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class HttpRequestMethodVisitor extends MethodVisitor {

    public HttpRequestMethodVisitor(MethodVisitor methodVisitor) {
        super(Opcodes.ASM9, methodVisitor);
    }

    @Override
    public void visitCode() {
        super.visitCode();

        // 在方法的开始处插入字节码，假设使用OkHttp并且请求是通过newBuilder创建的
        mv.visitVarInsn(Opcodes.ALOAD, 0); // 加载 this
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "okhttp3/Request", "newBuilder", "()Lokhttp3/Request$Builder;", false);
        mv.visitLdcInsn("Custom-Header"); // 自定义头部的Key
        mv.visitLdcInsn("YourHeaderValue"); // 自定义头部的Value
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "okhttp3/Request$Builder", "addHeader", "(Ljava/lang/String;Ljava/lang/String;)Lokhttp3/Request$Builder;", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "okhttp3/Request$Builder", "build", "()Lokhttp3/Request;", false);
        mv.visitVarInsn(Opcodes.ASTORE, 0); // 将修改后的Request对象重新赋值
    }
}
