package com.lxm.mvvm.plugin.adapter

import com.lxm.mvvm.plugin.adapter.ASMAdviceAdapter
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter;

class ASMVisitor extends ClassVisitor {

    ASMVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM6, classVisitor)
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        println("ASMVisitor:visit.." + name)
        super.visit(version, access, name, signature, superName, interfaces)
    }

    @Override
    FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        println("ASMVisitor:visitField。。" + name)
        return super.visitField(access, name, descriptor, signature, value)
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        println("ASMVisitor:visitMethod。。" + name + "....desc = " + descriptor + "....sign = " + signature)

        MethodVisitor mv = this.cv.visitMethod(access, name, descriptor, signature, exceptions);
// 如果文件的注
        return new ASMAdviceAdapter(mv,access,name,descriptor)

//        return super.visitMethod(access, name, descriptor, signature, exceptions)
    }

    @Override
    AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        println("ASMVisitor:visitAnnotation...." + descriptor)
        return super.visitAnnotation(descriptor, visible)
    }
}
