package test.java.lang;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Author: Cheers Lee
 * Date: 2021/5/30 16:08
 * Version: 1.0
 * <p>
 * ClassName: ObjectTest
 * Description: TODO
 */
public class ObjectTest extends GregorianCalendar implements Cloneable{

    String name;
    int likes;

    @Test
    public void getClassTest() {
        Object obj = new Object();
        Class<Object> cl = (Class<Object>) obj.getClass();
        System.out.println(cl.getName()); // java.lang.Object
    }

    @Test
    public void cloneTest() {
        ObjectTest obj = new ObjectTest();
        obj.name = "cloneTest";
        obj.likes = 100;

        System.out.println(obj.name);
        System.out.println(obj.likes);

        try{
            ObjectTest obj2 = (ObjectTest) obj.clone();
            System.out.println(obj2.name);
            System.out.println(obj2.likes);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void equalsTest() {
        // 如果要重写equals方法，就要重写hashcode方法
        Object obj1 = new Object();
        Object obj2 = new Object();

        System.out.println(obj1.equals(obj2));  // false

        Object obj3 = obj1;
        System.out.println(obj1.equals(obj3));  // true

        // String方法重写了equals方法和hashcode方法
        String str1 = new String();
        String str2 = new String();

        System.out.println(str1.equals(str2));  // true

        str1 = "equalsTest";
        str2 = "hashcodeTest";

        System.out.println(str1.equals(str2));  // false
    }

    @Test
    public void hashcodeTest() {
        // Object使用hashcode
        Object obj1 = new Object();
        System.out.println(obj1.hashCode());    // 1642534850
        Object obj2 = new Object();
        System.out.println(obj2.hashCode());    // 1724731843
        Object obj3 = new Object();
        System.out.println(obj3.hashCode());    // 1305193908


        // String使用hashcode
        String str1 = new String();             // 0
        System.out.println(str1.hashCode());
        String str2 = new String();
        System.out.println(str2.hashCode());    // 0

        // ArrayList的hashcode
        ArrayList<Integer> list = new ArrayList<Integer>();
        System.out.println(list.hashCode());    // 1
    }

    @Test
    public void toStringTest() {
        String[] array = {"Google", "Baidu", "Bing"};
        System.out.println(array.toString());   //[Ljava.lang.String;@61e717c2
        // 注意，String[]的toString方法就是有一点奇怪，包括int[]方法，这一点在java core中有提过
        int[] inttest = {1,2,3,4,5,6};
        System.out.println(inttest.toString());  // [I@66cd51c3
        System.out.println(array[1].toString()); // Baidu
    }

    @Test
    public void finalizeTest() {
        try{
            ObjectTest obj = new ObjectTest();
            System.out.println("" + obj.getTime());

            System.out.println("Finalizing...");
            obj.finalize();
            System.out.println("Finalized.");
        }catch(Throwable ex) {
            ex.printStackTrace();
        }
    }
}
