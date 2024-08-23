package com.ccts.highplugin;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

public  class HighTransformTask extends DefaultTask {
    private static final String GENERATED_PACKAGE = "com.example.myapplication";
    public static final String GENERATED_PACKAGE_PATH = GENERATED_PACKAGE.replace('.', '/');
    public static final String GENERATED_PACKAGE_DIR = GENERATED_PACKAGE.replace('.', File.separatorChar);

    private File inputDir = new File("D:\\Android_Project\\MyApplication3\\app\\build\\intermediates\\javac\\debug\\compileDebugJavaWithJavac\\classes\\com\\example\\myapplication");
    private File outputDir = new File("D:\\release\\modify\\com\\example\\myapplication");

    // 设定输入和输出目录
    public void setInputDir(File inputDir) {
        this.inputDir = inputDir;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    @TaskAction
    public void transform() throws IOException {
        if (inputDir == null || outputDir == null) {
            throw new IllegalArgumentException("Input and output directories must be set");
        }

        if(!inputDir.exists() ||! inputDir.isDirectory()) {
            throw new IllegalArgumentException("inputDir文件夹不存在");
        }
        if(! outputDir.exists() || ! outputDir.isDirectory()) {
            boolean isCreated =  outputDir.mkdirs();
            if(! isCreated){
                throw new IllegalArgumentException("outputDir文件夹不存在，并且创建失败");
            }
        }

        // 遍历输入目录中的 .class 文件
        Collection<File> files = FileUtils.listFiles(inputDir,
                new SuffixFileFilter(".class", IOCase.INSENSITIVE), TrueFileFilter.INSTANCE);
        for (File f : files) {
            String className = f.getAbsolutePath()
                    .substring(inputDir.getAbsolutePath().length() + 1,
                            f.getAbsolutePath().length() - ".class".length())
                    .replace(File.separatorChar, '.');

            try (FileInputStream fis = new FileInputStream(f.getAbsoluteFile())) {
                byte[] byteCode = referHackWhenInit(fis);
                try (FileOutputStream fos = new FileOutputStream(f.getAbsoluteFile())) {
                    fos.write(byteCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // 复制修改后的文件到输出目录
        FileUtils.copyDirectory(inputDir, outputDir);
    }
    private byte[] referHackWhenInit(FileInputStream fis) throws IOException {
        ClassReader cr = new ClassReader(fis);
        ClassWriter cw = new ClassWriter(cr, 0);
        OkHttpClientClassVisitor cv = new OkHttpClientClassVisitor(cw);
        cr.accept(cv, 0);
        return cw.toByteArray();
    }

}
