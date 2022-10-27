package com.winshare.demo.aop;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.reflect.Method;

public class JavassistProxy {

    public static <T> T crateProxy(Class<T> targeClss,AspectOne aspectOne) throws Exception {
        if(targeClss.isInterface()){
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.makeClass(targeClss.getPackage().getName() + "." + "Proxy$" + targeClss.getSimpleName());
            CtClass[] interfaces = new CtClass[]{classPool.get(targeClss.getName())};
            ctClass.setInterfaces(interfaces);
            CtMethod[] methods = interfaces[0].getMethods();

            for (CtMethod method:methods) {
                if(method.getName().equals("test")){

                   // method.setBody("this.aspectOne.targetBeforMethod(\"asdf\")");
                }
                //method.setBody(aspectOne.);
            }
            String s = ctClass.toString();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        TestInterface a  = crateProxy(TestInterface.class,new AspectOne());
        int  aa=0 ;
    }
}
