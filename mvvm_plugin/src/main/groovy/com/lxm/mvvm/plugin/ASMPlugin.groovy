package com.lxm.mvvm.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.lxm.mvvm.plugin.transform.ASMTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class ASMPlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {
        println("JavaAssistPlugin...")
        def android = project.extensions.getByType(AppExtension)
//        AppExtension就是build.gradle中android{...}这一块
        registerTransform(android)

    }

    def registerTransform(BaseExtension android) {
        println("register....")
        ASMTransform transform = new ASMTransform()
        android.registerTransform(transform)
    }
//    @Override
//    void apply(Project project) {
//        println("Splash")
//
//        def student = project.getExtensions().create("student", StudentExtension.class)
//
//        project.task('student') {
//            doLast {
//                println "name is ${student.name} ,age is ${student.age}"
//            }
//        }
//
//        project.task('testTask', type: TestTask)
//
//        //AppExtension就是build.gradle中android{...}这一块
//        def android = project.extensions.getByType(AppExtension)
////        registerTransform(android)
//    }
//
//    def static registerTransform(BaseExtension android) {
//        ASMTransform transform = new ASMTransform()
//        android.registerTransform(transform)
//    }

}
