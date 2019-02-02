package com.lxm.mvvm.plugin.transform

import com.android.build.api.transform.Context
import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

class ASMTransform extends Transform {
    @Override
    String getName() {
        return "ASMTransform"
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
                        println("遍历目录:" + file.absolutePath + ">>>" + file.name)
                        String name = file.name
                        //todo
                        if (name.endsWith("MainActivity.class") && !name.startsWith('R\$')
                                && "R.class" != name
                                && "BR.class" != name
                                && "BuildConfig.class" != name) {
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
                handlerJarFile(outputProvider,jarInput)
            }
        }
    }

    protected void injectClass(File file) {
        ClassReader classReader = new ClassReader(file.bytes)
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
        ClassVisitor cv = new com.lxm.mvvm.plugin.adapter.ASMVisitor(classWriter)
        classReader.accept(cv, ClassReader.EXPAND_FRAMES)
        byte[] code = classWriter.toByteArray()
        FileOutputStream fos = new FileOutputStream(file.absolutePath)
        fos.write(code)
        fos.close()
    }

    /**
     * Handle jar file
     *
     * @param transformInvocation
     * @param jarInput
     */
    void handlerJarFile(TransformOutputProvider outputProvider,JarInput jarInput) {
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
