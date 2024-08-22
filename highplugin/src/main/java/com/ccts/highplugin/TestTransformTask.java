package com.ccts.highplugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;

public class TestTransformTask extends DefaultTask {

    @TaskAction
    public void transform() throws IOException{


        System.out.println("22222222222222222222222");
    }
}
