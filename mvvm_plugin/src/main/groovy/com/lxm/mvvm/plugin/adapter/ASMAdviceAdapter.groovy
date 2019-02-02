package com.lxm.mvvm.plugin.adapter

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter

class ASMAdviceAdapter extends AdviceAdapter {

    boolean isAnnotation = false

    protected ASMAdviceAdapter(MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(Opcodes.ASM6, methodVisitor, access, name, descriptor)

    }

    @Override
    AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        println("ASMAdviceAdapter:" + descriptor)
        isAnnotation = true
        return super.visitAnnotation(descriptor, visible)
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter()
        if (!isAnnotation) return
        //插入字节码，ALOAD
        mv.visitVarInsn(ALOAD, 0);
        //插入字节码INVOKESTATIC，调用ActivityTimeManger.onCreateStart().
        // onCreate使用注解写入
        mv.visitMethodInsn(INVOKESTATIC, "com/lxm/mvvm/ActivityTimeManger",
                "onCreateStart",
                "(Landroid/app/Activity;)V")
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode)
        if (!isAnnotation) return
        //插入字节码，ALOAD
        mv.visitVarInsn(ALOAD, 0);
        //插入字节码INVOKESTATIC，调用ActivityTimeManger.onCreateStart().
        // onCreate使用注解写入
        mv.visitMethodInsn(INVOKESTATIC, "com/lxm/mvvm/ActivityTimeManger",
                "onCreateEnd",
                "(Landroid/app/Activity;)V")
    }
}
