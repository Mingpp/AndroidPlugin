package com.ccts.headplugin;
import com.android.build.gradle.AppExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class MyPlugin implements Plugin<Project>{

    @Override
    public void apply(Project project) {
        AppExtension baseExtension = project.getExtensions().getByType(AppExtension.class);
        baseExtension.registerTransform(new MyTransform(project));
    }
}
