package com.basejava.webapp;

import com.basejava.webapp.model.Resume;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Resume resume = new Resume(" dummy name");
        Field field = resume.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        System.out.println(field.get(resume));
        field.set(resume, "newuuid9999");
        System.out.println(resume);
        Class<Resume> classObject = Resume.class;
        Method method = classObject.getDeclaredMethod("toString");
        System.out.println("toString invocation: " + method.invoke(resume));
    }
}
