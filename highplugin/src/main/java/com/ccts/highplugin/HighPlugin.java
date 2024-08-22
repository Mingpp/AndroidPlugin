package com.ccts.highplugin;


import org.gradle.api.Plugin;
import org.gradle.api.Project;



public class HighPlugin  implements Plugin<Project>{

    @Override
    public void apply(Project project) {

        project.getTasks().register("myCustomTransformTask", HighTransformTask.class);

        System.out.println("MyClassTransformPlugin applied to project: " + project.getName());
    }


}