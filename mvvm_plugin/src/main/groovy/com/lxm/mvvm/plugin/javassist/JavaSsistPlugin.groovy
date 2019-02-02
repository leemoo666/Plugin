package com.lxm.mvvm.plugin.javassist

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.lxm.mvvm.plugin.StudentExtension
import com.lxm.mvvm.plugin.transform.JavassistTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class JavaSsistPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        println("JavaAssistPlugin...")
        def android = project.extensions.getByType(AppExtension)
        //AppExtension就是build.gradle中android{...}这一块
        registerTransform(android)



        /** **********************************/
        def student = project.getExtensions().create("student", StudentExtension.class)
        project.task('printClass') {
            doLast {
                println("task.....")
                if (project.plugins.hasPlugin(AppPlugin)) {
                    android.applicationVariants.all { variant ->
                        println "name is ${student.name} ,age is ${student.age}"
//                        createJavaTest(variant, student)
                    }
                }

            }
        }

    }

    def registerTransform(BaseExtension android) {
        println("register transform")
        JavassistTransform transform = new JavassistTransform()
        android.registerTransform(transform)
    }

    void createJavaTest(variant, student) {
        //要生成的内容
        def content = """package com.lxm.plugin;

                        /**
                         * Created by lxm on 2017/8/30.
                         */

                        public class BuildConfigBak {
                            public static final String str = "${student.name}";
                        }
                        """
        //获取到BuildConfig类的路径
        File outputDir = variant.getVariantData().getScope().getBuildConfigSourceOutputDir()
        def javaFile = new File(outputDir, "BuildConfigBak.java")
        javaFile.write(content, 'UTF-8')
    }

}
