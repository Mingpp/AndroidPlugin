package com.example.myapplication.asmIntercept;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

//import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class HttpRequestClassTransformer  {

//    @Override
//    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
//        if (className.equals("okhttp3/Call")) {  // 指定需要插装的类
//            ClassReader cr = new ClassReader(classfileBuffer);
//            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
//
//            HttpRequestClassVisitor cv = new HttpRequestClassVisitor(cw);
//            cr.accept(cv, 0);
//
//            return cw.toByteArray();
//        }
//        return classfileBuffer;
//    }
}
