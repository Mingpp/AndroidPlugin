package com.ccts.myplugin;



import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.gradle.api.Project;
import org.objectweb.asm.ClassReader;

import org.objectweb.asm.ClassWriter;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MyTransform extends Transform {
    private final Project mProject;
    private static final String GENERATED_PACKAGE = "com.example.myapplication";

    public static final String GENERATED_PACKAGE_PATH = GENERATED_PACKAGE.replace('.', '/');
    public static final String GENERATED_PACKAGE_DIR = GENERATED_PACKAGE.replace('.', File.separatorChar);

    public MyTransform(Project project) {
        this.mProject = project;
    }


    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        //得到Transform的输入  可以是jar 和 目录
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        //遍历 我们的输入
        for (TransformInput input : inputs) {
            // 得到 目录输入
            Collection<DirectoryInput> directoryInputs = input.getDirectoryInputs();

            //得到 jar的 input 要修改jar里面的字节码 需要先解压，然后 修改子字节码最后压缩放回原来的位置
            //这里就不便利 jar了
            // Collection<JarInput> jarInputs = input.getJarInputs();
            // 遍历目录输入
            for (DirectoryInput directoryInput : directoryInputs) {
                // 遍历 输入文件
                File src = directoryInput.getFile();
                //得到 输出，必须使用transformInvocation.getOutputProvider() 来获取文件的输出
                //供下一个transform 使用，不能破坏transform的 输入
                File dst = transformInvocation.getOutputProvider().getContentLocation(
                        directoryInput.getName(), directoryInput.getContentTypes(),
                        directoryInput.getScopes(), Format.DIRECTORY);
                //过滤 当前目录 中 DOT_CLASS = ".class" 以.class 结尾的文件，递归调用 文件夹
                Collection<File> files = FileUtils.listFiles(src,
                        new SuffixFileFilter(".class", IOCase.INSENSITIVE), TrueFileFilter.INSTANCE);
                for (File f : files) {
                    // src 的 path 直接定位到 编译之后形成的class文件所在的根目录
                    //  /Users/we/Documents/androidproject/AndroidDemo/app/build/intermediates/javac/debug/compileDebugJavaWithJavac/classes
                    // 出去src 根目录之后 后面就是具体的类的目录  但是是以'/' 结尾的，我们需要将'/'换成'.'就是全类名了
                    //  /Users/we/Documents/androidproject/AndroidDemo/app/build/intermediates/javac/debug/compileDebugJavaWithJavac/classes/androidx/activity/R.class
                    String className = f.getAbsolutePath()
                            .substring(src.getAbsolutePath().length() + 1,
                                    f.getAbsolutePath().length() - ".class".length())
                            .replace(File.separatorChar, '.');
                    // 符合 com.android.androiddemo 开头的类 都是我们自己的类，就是我们需要插桩的class文件了
                    if (className.startsWith(GENERATED_PACKAGE)) {
                        try {
                            FileInputStream fis = new FileInputStream(f.getAbsoluteFile());
                            //具体的插桩逻辑
                            byte[] byteCode = referHackWhenInit(fis);
                            fis.close();

                            FileOutputStream fos = new FileOutputStream(f.getAbsoluteFile());
                            fos.write(byteCode);
                            fos.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                //插桩完成之后 需要将你写的文件重新 拷贝到dst中 供下一个transform使用
                //这个不能忘记
                FileUtils.copyDirectory(src, dst);
            }
        }

    }

    private byte[] referHackWhenInit(InputStream fis) throws IOException {
        ClassReader cr = new ClassReader(fis);// 通过IO流，将一个class解析出来，解析失败会抛异常
        ClassWriter cw = new ClassWriter(cr, 0);//再构建一个writer
        OkHttpClientClassVisitor cv = new OkHttpClientClassVisitor(cw);


//        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
//            public MethodVisitor visitMethod(int access, final String name, String desc,
//                                             String signature, String[] exceptions) {
//                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
//                mv = new MethodVisitor(Opcodes.ASM5, mv) {
//                    @Override
//                    public void visitInsn(int opcode) {
//                        //就是在构造函数中 插入一段Class clazz = Antilazyload.class的代码
//                        if ("<init>".equals(name) && opcode == Opcodes.RETURN) {
//                            super.visitLdcInsn(Type.getType("Lcom/android/androiddemo/Antilazyload;"));//在class的构造函数中插入一行代码
//                        }
//                        super.visitInsn(opcode);
//                    }
//                };
//                return mv;
//            }
//        };
        cr.accept(cv, 0);
        return cw.toByteArray();
    }


    @Override
    public String getName() {
        return "dexchazhuang";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        Set<QualifiedContent.ContentType> types = new HashSet<>();
        types.add(QualifiedContent.DefaultContentType.CLASSES);
        return types;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        Set<QualifiedContent.Scope> scopes = new HashSet<>();
        scopes.add(QualifiedContent.Scope.PROJECT);
//        scopes.add(QualifiedContent.Scope.SUB_PROJECTS);
//        scopes.add(QualifiedContent.Scope.EXTERNAL_LIBRARIES);
        return scopes;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }
}
