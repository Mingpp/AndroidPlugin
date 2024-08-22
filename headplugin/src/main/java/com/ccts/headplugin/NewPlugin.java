//package com.ccts.headplugin;
//
//
//
//import com.android.build.api.artifact.ScopedArtifact;
//import com.android.build.api.variant.AndroidComponentsExtension;
//import com.android.build.api.variant.Variant;
//import com.android.build.api.variant.VariantBuilder;
//import com.android.build.api.variant.VariantOutput;
//import com.android.build.api.variant.AndroidComponentsExtension;
//import com.android.build.api.variant.VariantOutput;
//import com.android.build.gradle.AppExtension;
//
//import org.gradle.api.Plugin;
//import org.gradle.api.Project;
//import org.gradle.api.artifacts.Configuration;
//import org.gradle.api.file.FileCollection;
//import org.gradle.api.file.RegularFileProperty;
//import org.gradle.api.tasks.Input;
//import org.gradle.api.tasks.TaskAction;
//import org.objectweb.asm.ClassReader;
//import org.objectweb.asm.ClassVisitor;
//import org.objectweb.asm.ClassWriter;
//import org.objectweb.asm.MethodVisitor;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.Set;
//import org.gradle.api.tasks.InputFiles;
//import org.gradle.api.tasks.OutputDirectory;
//
//public class NewPlugin implements Plugin<Project> {
//    @Override
//    public void apply(Project project) {
//        // Get the AndroidComponentsExtension from the project
//        AndroidComponentsExtension<AppExtension, VariantBuilder, Variant> androidComponents =
//                project.getExtensions().findByType(AndroidComponentsExtension.class);
//
//        if (androidComponents != null) {
//            // Register a task for each variant
//            androidComponents.onVariants(variant -> {
//                project.getTasks().register("transform" + variant.getName().capitalize() + "ClassesWithBooster", BoosterTransformTask.class, task -> {
//                    task.setVariant(variant);
//                    task.setOutputDir(project.getLayout().getBuildDirectory().dir("outputs/transformed"));
//                });
//            });
//        } else {
//            project.getLogger().error("AndroidComponentsExtension not found. Make sure you are using a compatible version of the Android Gradle Plugin.");
//        }
//    }
//
//    public static class BoosterTransformTask extends DefaultTask {
//
//        private Variant variant;
//        private File outputDir;
//
//        @InputFiles
//        public FileCollection getInputFiles() {
//            return getProject().files(variant.getSourceSets().getByName("main").getJavaDirectories());
//        }
//
//        @OutputDirectory
//        public File getOutputDir() {
//            return outputDir;
//        }
//
//        public void setOutputDir(File outputDir) {
//            this.outputDir = outputDir;
//        }
//
//        @TaskAction
//        public void transform() throws IOException {
//            File srcDir = getInputFiles().getAsFileTree().getDir();
//            File dstDir = getOutputDir();
//
//            if (!dstDir.exists()) {
//                dstDir.mkdirs();
//            }
//
//            File[] files = srcDir.listFiles((dir, name) -> name.endsWith(".class"));
//            if (files != null) {
//                for (File file : files) {
//                    processClassFile(file, new File(dstDir, file.getName()));
//                }
//            }
//        }
//
//        private void processClassFile(File srcFile, File dstFile) throws IOException {
//            try (FileInputStream fis = new FileInputStream(srcFile);
//                 FileOutputStream fos = new FileOutputStream(dstFile)) {
//                ClassReader classReader = new ClassReader(fis);
//                ClassWriter classWriter = new ClassWriter(classReader, 0);
//                ClassVisitor classVisitor = new OkHttpClientClassVisitor(classWriter);
//                classReader.accept(classVisitor, 0);
//                fos.write(classWriter.toByteArray());
//            }
//        }
//
//        public Variant getVariant() {
//            return variant;
//        }
//
//        public void setVariant(Variant variant) {
//            this.variant = variant;
//        }
//    }
//
//
//}
