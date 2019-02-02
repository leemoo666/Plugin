package com.lxm.mvvm.plugin.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction;

class TestTask extends DefaultTask {

//    @Optional
//    String actionName

    @TaskAction
    def hello() {
//        println("hello myTask:" + actionName)
        println "Sender is ${project.student.name},age is : ${project.student.age}"
    }

}
