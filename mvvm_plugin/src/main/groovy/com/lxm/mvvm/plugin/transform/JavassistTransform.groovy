package com.lxm.mvvm.plugin.transform

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

class JavassistTransform extends Transform {
    @Override
    String getName() {
        return "JavassistTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(Context context,
                   Collection<TransformInput> inputs,
                   Collection<TransformInput> referencedInputs,
                   TransformOutputProvider outputProvider,
                   boolean isIncremental) throws IOException, TransformException, InterruptedException {
        println("transform。。。。")
        //此处会遍历所有文件
        /** 遍历输入文件 * */
        inputs.each { TransformInput input ->

            //遍历目录
            input.directoryInputs.each { DirectoryInput directoryInput ->
                if (directoryInput.file.isDirectory()) {//是否是目录
                    directoryInput.file.eachFileRecurse { File file ->
//                            println("遍历目录:"+file.absolutePath+">>>"+file.name)
                        //todo
                        if (file.name.endsWith("MainActivity.class")) {
                            println("遍历目录")
                            injectClass(file)
                        }
                    }
                    // 获取output目录
                    def dest = outputProvider
                            .getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                    // 将input的目录复制到output指定目录
                    FileUtils.copyDirectory(directoryInput.file, dest)

                }

            }
            //遍历jar
            input.jarInputs.each { JarInput jarInput ->
                handlerJarFile(outputProvider, jarInput)
            }
        }
    }

    protected void injectClass(File file) {
//        println("1111111:" + file.absolutePath)
//        ClassReader classReader = new ClassReader(file.bytes)
//        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
//        ClassVisitor cv = new JavassistVisitor(classWriter)
//        classReader.accept(cv, ClassReader.EXPAND_FRAMES)
//        byte[] code = classWriter.toByteArray()
//        println("2222:" + file.absolutePath)
//        FileOutputStream fos = new FileOutputStream(file.absolutePath)
//        fos.write(code)
//        fos.close()
    }

    /**
     * Handle jar file
     *
     * @param transformInvocation
     * @param jarInput
     */
    void handlerJarFile(TransformOutputProvider outputProvider, JarInput jarInput) {
        // 重名名输出文件,因为可能同名,会覆盖
        def jarName = jarInput.name
        def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
        if (jarName.endsWith(".jar")) {
            jarName = jarName.substring(0, jarName.length() - 4)
        }
        //  TODO: 处理jar进行字节码注入处理
        def dest = outputProvider.getContentLocation(jarName + md5Name,
                jarInput.contentTypes, jarInput.scopes, Format.JAR)
        FileUtils.copyFile(jarInput.file, dest)

    }
}
