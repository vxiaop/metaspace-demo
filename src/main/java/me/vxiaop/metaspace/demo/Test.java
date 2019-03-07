package me.vxiaop.metaspace.demo;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

/**
 * 利用asm包的ClassWriter动态创建1000万类对象来模拟持续类加载
 * JVM参数: -XX:+PrintGCDetails -XX:-UseCompressedClassPointers -XX:MetaspaceSize=64M -XX:MaxMetaspaceSize=128M
 *
 * @author vxiaop
 * @date 03/04/2018
 */
public class Test extends ClassLoader {
    public static void main(String[] args) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            // 循环1000万次生成1000万个不同的类对象
            for (int i = 0; i < 10000000; ++i) {
                ClassWriter classWriter = new ClassWriter(0);
                // 类名称为Class_i，访问修饰符是public，父类为java.lang.Object，不实现任何接口
                classWriter.visit(Opcodes.V1_1, Opcodes.ACC_PUBLIC, "Class_" + i, null, "java/lang/Object", null);
                Test test = new Test();
                byte[] code = classWriter.toByteArray();
                // 创建类对象
                Class<?> exampleClass = test.defineClass("Class_" + i, code, 0, code.length);
                classes.add(exampleClass);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
