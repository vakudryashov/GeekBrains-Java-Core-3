package Lesson_07;

import Lesson_07.Annotations.AfterSuite;
import Lesson_07.Annotations.BeforeSuite;
import Lesson_07.Annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    public static void start(Class testClass) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Method[] methods = testClass.getDeclaredMethods();
        List<Method> tests = new ArrayList<>();
        int afterSuiteCount = 0;
        int beforeSuiteCount = 0;
        for (Method method :methods) {
            if (method.getAnnotation(BeforeSuite.class) != null){
                if (++beforeSuiteCount > 1) throw new RuntimeException("В наборе тестов определено более одного метода с аннотацией BeforeSuite");
                tests.add(method);
            }else if (method.getAnnotation (Test.class) != null) {
                tests.add(method);
            }else if (method.getAnnotation (AfterSuite.class) != null) {
                if (++afterSuiteCount > 1) throw new RuntimeException("В наборе тестов определено более одного метода с аннотацией AfterSuite");
                tests.add(method);
            }
        }
        tests.sort((m1,m2) -> {
            if (m1.getAnnotation(BeforeSuite.class) != null || m2.getAnnotation(AfterSuite.class) != null) return -1;
            if (m1.getAnnotation(AfterSuite.class) != null || m2.getAnnotation(BeforeSuite.class) != null) return 1;
            return m1.getAnnotation(Test.class).priority() - m2.getAnnotation(Test.class).priority();

        });
        Constructor constructor  = testClass.getConstructor();
        Object obj = constructor.newInstance();
        for (Method method :tests) {
            method.invoke(obj);
        }
    }
}
