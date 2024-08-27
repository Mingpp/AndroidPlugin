headPlugin是一个低版本的插件，即com.android.tools.build:gradle:8.0.0以下的；highPlugin是一个高版本的插件，即com.android.tools.build:gradle:8.0.0及以上。

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

- 打包称maven包到本地 (这一步可以省略，我也不知道其用处)

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

