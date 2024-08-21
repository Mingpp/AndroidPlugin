package com.example.myapplication.asmIntercept;

import org.objectweb.asm.*;

public class HttpRequestClassVisitor extends ClassVisitor {

    public HttpRequestClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }


    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

        if (name.equals("execute") || name.equals("enqueue")) {  // 拦截OkHttp的execute或enqueue方法
            return new HttpRequestMethodVisitor(mv);
        }

        return mv;
    }



}
