package com.ccts.highplugin;


import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;


public class HighPlugin  implements Plugin<Project>{

    @Override
    public void apply(Project project) {

        project.getTasks().register("myCustomTransformTask", HighTransformTask.class, task -> {
            task.setGroup("MyPluginTasks");
            task.setDescription("Transforms classes using MyClassTransformTask.");
        });

        // 确保任务在 compileDebugJavaWithJavac 之后执行
        project.afterEvaluate(proj -> {
            Task compileDebugJavaTask = project.getTasks().findByName("compileDebugJavaWithJavac");
            Task dexBuilderDebugTask = project.getTasks().findByName("mergeDebugShaders");
            Task myTransformTask = project.getTasks().findByName("myCustomTransformTask");

            if (compileDebugJavaTask != null && myTransformTask != null) {
                myTransformTask.mustRunAfter(compileDebugJavaTask);
            }

            if (dexBuilderDebugTask != null && myTransformTask != null) {
                dexBuilderDebugTask.dependsOn(myTransformTask);
            }
        });

        System.out.println("MyClassTransformPlugin applied to project: " + project.getName());
    }


}