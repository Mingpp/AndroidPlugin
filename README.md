headPlugin是一个低版本的插件，即com.android.tools.build:gradle:8.0.0以下的；highPlugin是一个高版本的插件，com.android.tools.build:gradle:8.0.0及以上。

1. 应用插件：

- 发布插件到本地

```
 gradlePlugin {
    plugins {
        //MyTestPlugin  //这种方式也行
        create("MyPlugin") {
            //插件id
            id = 'com.ccts.highplugin'
            //插件的包名+类名
            implementationClass = 'com.ccts.highplugin.HighPlugin'
        }
    }
  }


```

- 打包称maven包到本地

```
publishing {
        // 配置仓库地址
        repositories {
            maven {
                url = uri('D:/release/repo')
            }
        }
    }
```



2. 在app的build.gradle.kts中添加 id("com.ccts.highplugin") version "1.0.0";

3. 高版本的用法，如果是在java代码中添加编译过程，保证myCustomTransformTask在compileDebugJavaWithJavac之后，在编译mergeDebugShaders之前进行（即javac和dex文件之间）
```java
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
```
