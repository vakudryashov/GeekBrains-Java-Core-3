package Lesson_07;

import Lesson_07.Annotations.AfterSuite;
import Lesson_07.Annotations.BeforeSuite;
import Lesson_07.Annotations.Test;

import java.lang.reflect.InvocationTargetException;

public class MainClass {
    public static void main(String[] args) {
        try {
            TestRunner.start(MainClass.class);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @BeforeSuite
    public void beforeSuite(){
        System.out.println("Run @BeforeSuite method");
    }

    @AfterSuite
    public void afterSuite(){
        System.out.println("Run @AfterSuite method");
    }

    @Test(priority=1)
    public void test1(){
        System.out.println("Run test1");
    }

    @Test(priority=2)
    public void test2(){
        System.out.println("Run test2");
    }

    @Test(priority=1)
    public void test3(){
        System.out.println("Run test3");
    }

    @Test(priority=10)
    public void test4(){
        System.out.println("Run test4");
    }
}
